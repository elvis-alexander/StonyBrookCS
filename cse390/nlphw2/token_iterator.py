from preprocess import sentence_segmentation, split_token, tokenization
from constants import start_sym, end_sym


class TokenIterator:
    def __init__(self, f):
        self.sentences = sentence_segmentation(f)
        self.curr_line = 0
        self.curr_index = 0
        self.num_lines = len(self.sentences)

    def __iter__(self):
        return self

    # returns the next two bigrams/tags sequences
    def __next__(self):
        # reached end of file
        if self.curr_line + 1 == self.num_lines:
            raise StopIteration
        curr_index = self.curr_index
        curr_line = self.curr_line
        sentences = self.sentences
        if curr_index == 0:
            sentences[curr_line] = tokenization(sentences[curr_line])
        tok1 = split_token(sentences[curr_line][curr_index])
        tok2 = split_token(sentences[curr_line][curr_index + 1])
        self.curr_index += 1
        end_of_sentence = False
        if self.curr_index + 1 == len(self.sentences[curr_line]):
            self.curr_index = 0
            self.curr_line += 1
            end_of_sentence = True
        # return (word1, tag1), (word2, tag2)
        return (tok1[0], tok1[1]), (tok2[0], tok2[1]), end_of_sentence


# t = TokenIterator(open('data/tags.txt'))
# for tok in t:
#     print(tok)
