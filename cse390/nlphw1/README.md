# Elvis Fernandez
> **Please read from [Github](https://github.com/elvis-alexander/nlphw1).**<br>
> **Please run with python version 2** <br>


File Structure:
```
nlphw1
│   README.md
│   bigram-query.py
│   constants.py
│   lm_builder.py
│   ngram.py
│   perplexity.py
│   preprocess.py
│   utilities.py
│
└───pdf
│      brief-report.pdf
│      written-assignment.txt
│└───lm
│      bigram.lm
│      top-bigrams.txt
│      unigram.lm
│
└───data
    │   dev.txt
    │   test.txt
    │   train.txt
```
- bigram-query.py - used to look up probabilities of a given bigram and smoothing method
- constants.py - used to specify constants for the program
- lm_builder.py - builds bigram.lm, unigram.lm, top-bigram.txt files from training data
- ngram.py - represents a training corpus, contains bigrams from training and holds most of the calculations
- perplexity.py - used to calculate perplexities
- preprocess.py - used to preprocess corpuses
- utilities.py - used to load data from training data



<h3>Language Model Builder</h3>
To generate the bigram.lm, unigram.lm and top-bigrams.txt files simply run like this:
```
python lm_builder.py
```
This command will generate the language model files and will place them in the data directory. It retrieves training data from lm/test.txt. If you would like to create langauge models from different training and dev files, you can specify paths under constants.py
Modify the following, if need be.
```
train_file = 'data/train.txt'
dev_file = 'data/dev.txt'
```
<h3>Bigram Query Application</h3>
```
python bigram-query.py <path_to_bigram_file> <path_to_unigram_file> <x> <y> <smoothing method: {M, L, I, K}>
```
Example:
```
python bigram-query.py lm/bigram.lm lm/unigram.lm in america L
```
Output:
```
Pr(america|in) = 0.000956937799043
```

<!--- [] do something-->
<!--- [x] dont do the first thing-->
<h3>Language Model Evaluator</h3>
```
python perplexity.py <path_to_bigram_lm> <path_to_unigram_lm> <path_to_test_file>
```
Example
```
python perplexity.py lm/bigram.lm lm/unigram.lm data/test.txt
```
Output:
```
Laplace Bigram: 1214.68641571
Interpolated Bigram: 401.104384608
Laplace Unigram: 515.72961315
```
