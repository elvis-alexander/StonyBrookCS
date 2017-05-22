############################
# just a helper for
# loading lm files
# into in-memory datastructures
# for calculations
############################


# helper method for loading unigram.lm files
def load_unigram(f):
    words = dict()
    total_tokens = 0
    sentences = f.readlines()
    for sen in sentences:
        if sen == '' or len(sen) < 2:
            continue
        line = sen.split('\t')
        # word to occurrences
        words[line[0]] = int(line[1])
        total_tokens += int(line[1])
    f.close()
    return {'words': words, 'total_tokens': total_tokens}


# helper method for loading bigram.lm files
# to a dicionary mapping bigrams to all probabilities
def load_bigram(f):
    sentences = f.readlines()
    bigrams = dict()
    for sen in sentences:
        line = sen.split('\t')
        x = line[0]
        y = line[1]
        # bigrams[x,y] = (xy_occurrences, mle, laplace, interpolation, pr_k)
        bigrams[(x, y)] = (int(line[2]), float(line[3]), float(line[4]), float(line[5]), float(line[6]))
    f.close()
    return bigrams


# maps smoothing to index (used to retrieve a probability)
def get_index(smooth_method):
    return {
        'M':1,
        'L':2,
        'I':3,
        'K':4
    }.get(smooth_method, -1)


