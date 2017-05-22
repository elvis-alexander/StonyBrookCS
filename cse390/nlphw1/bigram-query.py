from ngram import Ngram
from utilities import *
from sys import argv, exit

# this is the main method for calculating probability of a bigram specified by the user
# algorithm: is to detect if the bigram has already been seen in training if it has
# this simply returns the probability, otherwise it calculates probability on the fly
if __name__ == '__main__':
    # assure all arguments exist
    if len(argv) < 6:
        print 'Please run with the following format: python bigram-query.py <path_to_bigram_lm> <path_to_unigram_lm> <x> <y> <smoothing: M, L, I, K>'
        exit()
    # assure correct smoothing method {M,L,I,K}
    smooth_method = argv[5].upper()
    smooth_index = get_index(smooth_method)
    if smooth_index == -1:
        print 'Please use only one of the following smooth methods: {M, L, I, K}'
        exit()
    # load bigram
    bigrams = load_bigram(open(argv[1]))
    # load unigram
    uni = load_unigram(open(argv[2]))
    words = uni['words']
    total_tokens = uni['total_tokens']
    training_gram = Ngram(total_tokens, words, bigrams)
    # load x, y, smooth_method
    x = argv[3]
    y = argv[4]
    # exit if x is non-existent in training
    if argv[3] not in words:
        print 'We are incredibly sorry, but the word you requested was not found in the training set'
        exit()
    # return probability if bigram has been seen in training
    if (x, y) in bigrams:
        print 'Pr({}|{}) = {}'.format(y, x, training_gram.get_prob(x, y, smooth_index))
    else:
        # bigram (x,y) has not been seen, calculate probability for specific smoothing for bigram (x,y)
        if smooth_method == 'M':
            print "Pr({}|{}) = {}".format(y, x, training_gram.mle(x, y))
        elif smooth_method == 'L':
            print "Pr({}|{}) = {}".format(y, x, training_gram.laplace_bigram(x, y))
        elif smooth_method == 'I':
            print "Pr({}|{}) = {}".format(y, x, training_gram.interpolation(x, y, 0.3))
        else:
            print "Pr({}|{}) = {}".format(y, x, training_gram.pr_k(x, y))
