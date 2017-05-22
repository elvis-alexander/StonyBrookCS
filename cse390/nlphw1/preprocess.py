# splits s on '.'
def sentence_segmentation(f):
    return f.read().replace('\n', ' ').split('.')


# splits sentence on space
def tokenization(s):
    return s.split()