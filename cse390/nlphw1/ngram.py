from math import log
from os import path, makedirs
from preprocess import sentence_segmentation, tokenization
from constants import uni_f, bi_f, top_bi_f, start_sym, end_sym, dev_file


# Ngram class, provides functionality to
class Ngram:
    def __init__(self, total_tokens, words, bigrams):
        # set instance variables
        self.total_tokens = total_tokens
        self.words = words
        self.bigrams = bigrams

    # creates the unigram language model file
    def create_unigram_lm(self):
        f = self.create_file(uni_f)
        for x in sorted(self.words):
            out = "{}\t{}\n".format(x, str(self.occurrences(x)))
            f.write(out)
        f.close()

    # creates the bigram language model file
    def create_bigram_lm(self):
        f = self.create_file(bi_f)
        bigrams = self.bigrams
        perplexity = self.min_perplexity()
        min_perplexity = perplexity[0]
        min_lam = perplexity[1]
        for bigram in sorted(bigrams):
            x = bigram[0]
            y = bigram[1]
            out = "{}\t{}\t{}\t{}\t{}\t{}\t{}\n".format(
                x,
                y,
                str(self.xy_count(x, y)),
                str(self.mle(x, y)),
                str(self.laplace_bigram(x, y)),
                str(self.interpolation(x, y, min_lam)),
                str(self.pr_ad(x, y))
            )
            f.write(out)
        f.close()

    # creates top bigrams file
    def create_top_bigrams(self):
        f = self.create_file(top_bi_f)
        bigrams = self.bigrams
        top = list()
        for bigram in bigrams:
            x = bigram[0]
            y = bigram[1]
            if x == end_sym and y == start_sym:
                continue
            if x == start_sym or x == end_sym:
                continue
            if y == end_sym or y == start_sym:
                continue
            top.append((self.joint_laplace(x, y), (x, y)))
        for t in sorted(top, reverse=True)[:20]:
            x = t[1][0]
            y = t[1][1]
            out = "{}\t{}\t{}\n".format(x, y, str(t[0]))
            f.write(out)
        f.close()

    # calculates min perplexity from dev file
    def min_perplexity(self):
        dev = open(dev_file, 'r')
        perplexities = []
        sentences = sentence_segmentation(dev)
        lam_values = [0.1, 0.3, 0.5, 0.7, 0.9]
        for lam in lam_values:
            sigma = 0
            dev_len = 0
            for sen in sentences:
                tokens = tokenization(start_sym + ' ' + sen + ' ' + end_sym)
                x = start_sym
                for y in tokens[1:]:
                    if y == '':
                        continue
                    y = y.lower()
                    inter_pol = self.dev_interpolation(x, y, lam)
                    if inter_pol != 0:
                        sigma += log(inter_pol, 2)
                        dev_len += 1
                    x = y
            perplexities.append((pow(2, (-1 / float(dev_len)) * sigma), lam))
        # returns tuple (perplexity, lambda)
        return min(perplexities)

    # returns occurrences of a word through training
    def occurrences(self, word):
        return self.words.get(word, 0)

    # returns the num of (x,y) occurrences
    def xy_count(self, x, y):
        if (x, y) not in self.bigrams:
            return 0
        if type(self.bigrams[(x, y)]) is tuple:
            return self.bigrams.get((x, y))[0]
        return self.bigrams.get((x, y), 0)

    # mle for bigram y | x = occur of (x,y) / occur(x)
    def mle(self, x, y):
        if self.occurrences(x) == 0:
            return 0
        return float(self.xy_count(x, y)) / self.occurrences(x)

    # laplace smoothing for unigram
    def laplace_unigram(self, x):
        return float(self.occurrences(x) + 1) / (self.total_tokens + len(self.words) + 1)

    # laplace smoothing for bigram
    def laplace_bigram(self, x, y):
        return float(self.xy_count(x, y) + 1) / (self.occurrences(x) + len(self.words) + 1)

    # used for calculating top-bigrams
    # returns unigram + bigram laplace smoothing
    # pr_l(x, y) = pr_l(x) * pr_l(y|x)
    def joint_laplace(self, x, y):
        return self.laplace_unigram(x) * self.laplace_bigram(x, y)

    # lam * pr_mle(y | x) + (1 - lam) * pr_l(y)
    def interpolation(self, x, y, lam):
        return (lam * self.mle(x, y)) + ((1 - lam) * self.laplace_unigram(y))

    # wrapper for interpolation for dev.txt
    def dev_interpolation(self, x, y, lam):
        # skip this word on dev set
        if x not in self.words:
            return 0
        return self.interpolation(x, y, lam)

    # returns pr_ad given that #(x, y) > 0, pr_k = pr_ad
    def pr_ad(self, x, y):
        return float(self.xy_count(x, y) - 0.5) / (self.occurrences(x))

    # returns pr_k given #(x, y) = 0
    def pr_k(self, x, y):
        # calculating beta
        prl_y = self.laplace_unigram(y)
        sum_pr_ad = 0
        sum_prl_w = 0
        for w in self.words:
            if (x, w) in self.bigrams:
                sum_pr_ad += self.pr_ad(x, w)
                sum_prl_w += self.laplace_unigram(w)
        alpha = 1 - sum_pr_ad
        beta = prl_y / (1 - sum_prl_w)
        return alpha * beta

    # helper for creating files
    @staticmethod
    def create_file(fn):
        if not path.exists('lm'):
            makedirs('lm')
        return open(fn, 'w+')

    #
    def get_prob(self, x, y, prob_index):
        if (x, y) not in self.bigrams:
            return 0
        return self.bigrams[(x, y)][prob_index]