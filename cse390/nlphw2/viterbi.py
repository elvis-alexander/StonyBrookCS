from constants import start_sym
from math import log


def get_transmission(A, t1, t2):
    return log(float(A.get((t1, t2), 0.000000000000000000000000000001)))


def get_emission(B, tag, word, smoothing):
    v = B.get((tag, word), 0)
    if v == 0:
        v = B.get((tag, "<UNK>"), 0)
    if v == 0:
        print(tag)
        print('panic', word, tag)
    return log(v)


# file containing the viterbi algorithm
def viterbi(S, A, B, T, smoothing, f, actualTags):
    # Initialization.
    n = len(S)
    height = n
    width = len(T)
    delta = [[0 for x in range(width)] for y in range(height)]
    psi = [[0 for x in range(width)] for y in range(height)]
    # Initialize  and  for the first word in the sentence S1.
    first_word = S[0]
    for i in range(0, len(T)):
        t = T[i]
        delta[0][i] = float(A.get((start_sym, t), 0.00000000000000000000000000000000000000000000000000000000000000001)) * float(B.get((t, first_word), 0.004))
        psi[0][i] = 0
    # Forward pass
    for i in range(1, n):
        for j in range(0, len(T)):
            max_ = -1
            sym = None
            for k in range(0, len(T)):
                cur_val = float(delta[i - 1][k]) * float(get_transmission(A, T[k], T[j])) * float(get_emission(B, T[j], S[i], smoothing))
                if cur_val > max_:
                    max_ = cur_val
                    sym = T[k]
            delta[i][j] = max_
            psi[i][j] = T.index(sym)
    best_tags = [None] * (n)
    max_ = -1
    sym = None
    for k in range(0, len(T)):
        cur_val = delta[n-1][k]
        if cur_val > max_:
            max_ = cur_val
            sym = k
    best_tags[n-1] = sym
    for k in range(n - 2, -1, -1):
        best_tags[k] = psi[k + 1][best_tags[k+1]]
    #
    for i in range(0, len(best_tags)):
        t = best_tags[i]
        out = "{}/{}/{} ".format(S[i], actualTags[i], T[best_tags[i]])
        f.write(out)
    f.write("\n")
    # exit()


# delta[0][i] = float(A.get((start_sym, t), 0.000000000000000000000000000001)) * float(B.get((t, first_word), 0.005))