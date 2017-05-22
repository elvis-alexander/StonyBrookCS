# helper to load dictionary
# mapping (word, tag) to Pr(t|w)
def load_emissions(emissionsfile):
    emissions = dict()
    tag_occurrences = dict()
    sentences = emissionsfile.readlines()
    for sen in sentences:
        if sen == '':
            continue
        line = sen.split('\t')
        tag = line[0]
        word = line[1]
        if word == '<UNK>':
            continue
        pr_tw = line[4]
        emissions[(word, tag)] = float(pr_tw)
        tag_occurrences[tag] = tag_occurrences.get(tag, 0) + 1
    return emissions, tag_occurrences
