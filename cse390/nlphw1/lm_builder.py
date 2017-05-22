from ngram import Ngram
from preprocess import sentence_segmentation, tokenization
from constants import start_sym, end_sym, train_file

# parses train file
# creates a dictionary mapping words to occurences
# creates a second dictionary mapping bigrams to occurences
if __name__ == '__main__':
    train = open(train_file, 'r')
    bigrams = dict()
    words = dict()
    total_tokens = 0
    for sen in sentence_segmentation(train):
        if sen == '':
            continue
        tokens = tokenization("{} {} {}".format(start_sym, sen, end_sym))
        x = tokens[0]
        words[x] = words.get(x, 0) + 1
        for y in tokens[1:]:
            if y == '':
                continue
            y = y.lower()
            bigrams[(x, y)] = bigrams.get((x, y), 0) + 1
            words[y] = words.get(y, 0) + 1
            x = y
        total_tokens += len(tokens)

    training_gram = Ngram(total_tokens, words, bigrams)
    # creates bigram.lm file
    training_gram.create_bigram_lm()
    # creates unigram.lm file
    training_gram.create_unigram_lm()
    # creates top-bigrams.txt file
    training_gram.create_top_bigrams()