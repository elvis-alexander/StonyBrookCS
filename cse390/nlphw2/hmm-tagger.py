from sys import argv
from viterbi import viterbi
from constants import start_sym
import os

if __name__ == '__main__':
    if not os.path.exists("results"):
        os.makedirs("results")
    # either M or L
    smoothing_method = argv[4]
    # output file should contain <probability> <word>/<true tag>/<predicted tag>
    f = open(argv[5], 'w+')
    # parse transitions from training data
    # T is set of tags that exist in training
    T = set()
    A = dict()
    transitions_file = open(argv[2], 'r')
    for sen in transitions_file.readlines():
        tokens = sen.split()
        t1 = tokens[0]
        t2 = tokens[1]
        if t1 == start_sym:
            continue
        A[(t1, t2)] = float(tokens[2])
        T.add(t1)
    transitions_file.close()
    # convert to list, list of part of speech tags
    T = list(T)
    # parse emissions probability
    B = dict()
    emissions_file = open(argv[3], 'r')
    for sen in emissions_file.readlines():
        tokens = sen.split()
        tag = tokens[0]
        word = tokens[1]
        if smoothing_method == 'M':
            pr_wt = float(tokens[2])
        else:
            pr_wt = float(tokens[3])
        B[(tag, word)] = pr_wt
    emissions_file.close()
    # Viterbi algorithm, parse test file
    test_file = open(argv[1],'r')
    curr_line = 800
    for sen in test_file.readlines():
        tokens = sen.split()
        S = []
        actualTags = []
        for tok in tokens[1:]:
            slash_index = tok.rfind("/")
            word = tok[0:slash_index]
            S.append(word)
            actualTags.append(tok[slash_index + 1:len(tok)])
        f.write(str(curr_line)+ " ")
        viterbi(S, A, B, T, smoothing_method, f, actualTags)
        curr_line += 1
    test_file.close()
    f.close()

# 800	Soon/RB Many/JJ appeared/VBD ./. *ICH*-1/-NONE-
