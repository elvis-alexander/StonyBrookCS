from sys import argv
from constants import *
from token_iterator import TokenIterator
from traininggram import TrainingGram
import os

# python train-tagger.py train.txt transitions.txt emissions.txt laplace-tag-unigrams.txt

if __name__ == '__main__':
    if not os.path.exists("training"):
        os.makedirs("training")
    train = open(argv[1], 'r')
    # maps tags to occurrences
    tag_occurrences = dict()
    # maps (tag1, tag2) occurrences
    tag_bigrams = dict()
    # map words to occurrences
    word_occurrences = dict()
    # maps (word, tag) to occurrences
    emission_bigrams = dict()
    # parse every set of (word | tag) sequences
    tokens = TokenIterator(train)
    # total tokens
    total_tokens = 0
    train.close()
    for tok in tokens:
        # count tags
        tag1 = tok[0][1]
        tag2 = tok[1][1]
        tag_occurrences[tag1] = tag_occurrences.get(tag1, 0) + 1
        tag_bigrams[(tag1, tag2)] = tag_bigrams.get((tag1, tag2), 0) + 1
        total_tokens += 1
        if tag1 == start_sym:
            continue
        # count words
        word1 = tok[0][0]
        word2 = tok[1][0]
        word_occurrences[word1] = word_occurrences.get(word1, 0) + 1
        emission_bigrams[(word1, tag1)] = emission_bigrams.get((word1, tag1), 0) + 1
    tgram = TrainingGram(total_tokens, tag_occurrences, tag_bigrams, word_occurrences, emission_bigrams)
    tgram.create_transitions(argv[2])
    tgram.create_emissions(argv[3])
    tgram.create_tag_unigrams(argv[4])