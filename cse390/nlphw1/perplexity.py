from math import log
from sys import argv, exit
from ngram import start_sym, end_sym
from preprocess import tokenization, sentence_segmentation
from utilities import *
from ngram import Ngram

if __name__ == '__main__':
    if len(argv) < 4:
        print 'Please run with the following format python perplexity.py <path_to_bigram_lm> <path_to_unigram_lm> <path_to_test_file>'
        exit()
    # load unigram
    uni = load_unigram(open(argv[2]))
    words = uni['words']
    total_tokens = uni['total_tokens']
    # load bigram
    bigrams = load_bigram(open(argv[1]))
    training_gram = Ngram(total_tokens, words, bigrams)
    # calculate perplexities
    test_words = []
    sentences = sentence_segmentation(open(argv[3]))
    for sen in sentences:
        tokens = tokenization(start_sym + ' ' + sen + ' ' + end_sym)
        for tok in tokens:
            if tok == '':
                continue
            tok = tok.lower()
            test_words.append(tok)
    test_size = len(test_words)
    # calculating perplexities
    bi_perplexity = 0
    inter_perplexity = 0
    uni_perplexity = 0
    x = start_sym
    # calculate summation
    uni_perplexity += log(training_gram.laplace_unigram(x), 2)
    for y in test_words[1:]:
        bi_perplexity += log(training_gram.laplace_bigram(x, y), 2)
        inter_perplexity += log(training_gram.interpolation(x, y, 0.3), 2)
        uni_perplexity += log(training_gram.laplace_unigram(y), 2)
        x = y
    # calculate perplexities
    bi_perplexity = pow(2, (-1 / float(test_size)) * bi_perplexity)
    inter_perplexity = pow(2, (-1 / float(test_size)) * inter_perplexity)
    uni_perplexity = pow(2, (-1 / float(test_size)) * uni_perplexity)
    # output perplexities
    out = "Laplace Bigram: {}\nInterpolated Bigram: {}\nLaplace Unigram: {}".format(
        str(bi_perplexity),
        str(inter_perplexity),
        str(uni_perplexity)
    )
    print out