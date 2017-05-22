from constants import start_sym, end_sym


# splits contents of file f on new lines
def sentence_segmentation(f):
    return f.read().split('\n')


# adds a <s>/<s> symbol to the start and a </s></s> to the end a sentence
def tokenization(s):
    content = s.split('\t')[1]
    content = "{}/{} {} {}/{}".format(start_sym, start_sym, content, end_sym, end_sym)
    return content.split(' ')


# split on a word and part of speech ('/')
def split_token(s):
    if s == "<s>/<s>":
        return start_sym, start_sym
    elif s == "</s>/</s>":
        return end_sym, end_sym
    # split on last / character
    slash_index = s.rfind("/")
    return s[0:slash_index], s[slash_index + 1:len(s)]


