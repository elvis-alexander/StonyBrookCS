# includes some constants to be used throughout the program
# start and end symbols
start_sym = '<s>'
end_sym = '</s>'
# specifies where lm files are to be placed
uni_f = 'lm/unigram.lm'
bi_f =  'lm/bigram.lm'
top_bi_f = 'lm/top-bigrams.txt'
# change these file locations if wish to
#  change on different training and dev files
# specifies where training and dev files are
train_file = 'data/train.txt'
dev_file = 'data/dev.txt'