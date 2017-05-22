

class TrainingGram():
    def __init__(self, total_tokens, tag_occurrences, tag_bigrams, word_occurrences, emission_bigrams):
        self.total_tokens = total_tokens
        # tags/transitions will contain start symbol and all words not including end symbol.
        self.tag_occurrences = tag_occurrences
        self.transition_bigrams = tag_bigrams
        # maps words to occurrences
        self.word_occurrences = word_occurrences
        # emission bigrams will only include word sequences in the sentence.
        self.emission_bigrams = emission_bigrams

    # creates the transitions file
    def create_transitions(self, fname):
        f = open(fname, 'w+')
        for bigrams in sorted(self.transition_bigrams):
            t1 = bigrams[0]
            t2 = bigrams[1]
            out = "{}\t{}\t{}\n".format(t1, t2, str(self.transition_mle(t1, t2)))
            f.write(out)
        f.close()

    # creates the emissions file
    def create_emissions(self, fname):
        f = open(fname, "w+")
        prev_tag = None
        prev_laplace_sum = 0
        for bigram in sorted(self.emission_bigrams, key=lambda x: x[1]):
            word = bigram[0]
            tag = bigram[1]
            # <tag> <word> <pr_mle(word | tag)> <pr_l(word | tag)> <pr_mle(tag | word>)
            out = "{}\t{}\t{}\t{}\t{}\n".format(tag, word, self.emission_mle(word, tag), self.emission_laplace_unigram(word, tag), self.tag_for_word(tag, word))
            if prev_tag == None or tag != prev_tag:
                if prev_tag != None:
                    f.write("{}\t{}\t{}\t{}\n".format(prev_tag, "<UNK>", 1 - prev_laplace_sum, 1 - prev_laplace_sum))
                prev_tag = tag
                prev_laplace_sum = self.emission_laplace_unigram(word, tag)
            else:
                prev_laplace_sum += self.emission_laplace_unigram(word, tag)
            f.write(out)
        # write <unk> for last tag
        f.write("{}\t{}\t{}\t{}\n".format(prev_tag, "<UNK>", 1 - prev_laplace_sum, 1 - prev_laplace_sum))
        f.close()

    def create_tag_unigrams(self, fname):
        f = open(fname, "w+")
        for t in sorted(self.tag_occurrences):
            # if t
            out = "{}\t{}\n".format(t, self.laplace_tag_unigram(t))
            f.write(out)
        f.close()

    # helper functions for calculations

    # number of occurrences for a given tag
    def tag_count(self, t):
        return self.tag_occurrences.get(t, 0)

    # number of tag sequences
    def transition_count(self, t1, t2):
        return self.transition_bigrams.get((t1, t2), 0)

    # number of word-tag sequences
    def emission_count(self, w, t):
        return self.emission_bigrams.get((w, t), 0)

    # pr_mle(t1, t2) => t2 | t1
    def transition_mle(self, t1, t2):
        if self.tag_count(t1) == 0:
            return 0
        return float(self.transition_count(t1, t2)) / self.tag_count(t1)

    # pr_mle(word | tag)
    def emission_mle(self, word, tag):
        if self.tag_count(tag) == 0:
            return 0
        return float(self.emission_count(word, tag)) / self.tag_count(tag)

    # pr_l(word | tag)
    def emission_laplace_unigram(self, word, tag):
        return float(self.emission_count(word, tag) + 1) / (self.tag_count(tag) + len(self.word_occurrences) + 1)

    # pr_mle(tag | word), weird one for freq-tagger
    def tag_for_word(self, tag, word):
        if self.word_occurrences.get(word, 0) == 0:
            return 0
        return float(self.emission_count(word, tag)) / float(self.word_occurrences[word])

    # pr_l(tag) for
    def laplace_tag_unigram(self, t):
        return float(self.tag_count(t) + 1) / (len(self.tag_occurrences) + self.total_tokens)