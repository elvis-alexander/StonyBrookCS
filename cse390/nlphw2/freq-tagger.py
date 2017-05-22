# python freq-tagger.py test.txt emissions.txt test-fbtagger.txt

from sys import argv
from constants import *
from utilities import load_emissions
from token_iterator import TokenIterator
import os

if __name__ == '__main__':
    if not os.path.exists("results"):
        os.makedirs("results")
    emissions_file = open(argv[2])
    loaded_data = load_emissions(emissions_file)
    emissions = loaded_data[0]
    tag_set = loaded_data[1]
    emissions_file.close()
    # parse every set of (word | tag) sequences
    test_file = open(argv[1], 'r')
    tokens = TokenIterator(test_file)
    test_file.close()
    # most occurrences
    most_frequent_tag = 'NNP'
    # iterate through every two (w1, t1) (w2, t2), will include (startsym, startym), () and (), (endsym, endsym)
    fbtagger_file = open(argv[3], "w+")
    curr_line = 800
    out = "{} ".format(curr_line)
    fbtagger_file.write(out)
    for tok in tokens:
        cur_tag = tok[0][1]
        word = tok[0][0]
        if cur_tag == start_sym:
            continue
        current_max = 0
        max_tag = None
        tie = False
        max_set = None
        #
        for tag in tag_set:
            if (word, tag) in emissions:
                if emissions[(word, tag)] > current_max:
                    current_max = emissions[(word, tag)]
                    max_set = list()
                    max_set.append(tag)
                elif emissions[(word, tag)] == current_max:
                    max_set.append(tag)
        if max_set is None:
            max_set = list()
            max_set.append(most_frequent_tag)
        if len(max_set) > 1:
            max_val = 0
            max_tag = None
            for t in max_set:
                if tag_set[t] >= max_val:
                    max_val = tag_set[t]
                    max_tag = t
        else:
            max_tag = max_set[0]
        if tok[2] is True:
            curr_line += 1
            out = "{}/{}/{} \n{} ".format(word, cur_tag, max_tag,curr_line)
        else:
            out = "{}/{}/{} ".format(word, cur_tag, max_tag)
        fbtagger_file.write(out)
    fbtagger_file.close()