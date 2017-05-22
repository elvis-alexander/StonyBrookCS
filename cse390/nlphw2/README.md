Elvis Fernandez
109237817

```
python3 train-tagger.py data/test.txt training/transitions.txt training/emissions.txt training/laplace-tag-unigrams.txt
```


This will create the training data used for testing the different part-of-speech tagger implementations


```
python3 freq-tagger.py data/test.txt training/emissions.txt results/test-fbtagger.txt
```

This will generate the frequent-based tagger to test-fbtagger.txt
Assumptions: For ties between tags I have chosen the more frequent tag from training data.
for unseen words I have taken the most frequent tag from the training data (NNP)


```
python3 hmm-tagger.py data/test.txt training/transitions.txt training/emissions.txt M results/test-hmm-mle-tagger.txt
python3 hmm-tagger.py data/test.txt training/transitions.txt training/emissions.txt L results/test-hmm-laplace-tagger.txt
```
This will generate the HMM tagger for the Viterbi Algorithm


```
python3 evaluator.py results/test-fbtagger.txt evaluator/test-fbtagger-evaluation.txt
python3 evaluator.py results/test-hmm-laplace-tagger.txt evaluator/test-hmm-laplace-tagger-evaluation.txt
python3 evaluator.py results/test-hmm-mle-tagger.txt evaluator/test-hmm-mle-tagger-evaluation.txt
```

This will generate the evaluation files for each of the different tagging methods implemented for this assignment.


Error Analysis:
Analyze the outputs from each of the three methods (i) Baseline tagger, (ii)
HMM with MLE and (iii) HMM with Laplace. Identify one type of error that
seems specific to each method, give two examples, and explain what you think
is the cause of that error.

Baseline: I think the problem with the baseline tagger is that their is that it will only reflect well for sequences of tags that are popular and word-tag sequences that have seen in data. There is a lot of imbalance as far as dominating tags over less frequent tags. 
HMM with Laplace: Laplace is able to resolve known issues as far seeing tag sequences not seen in training by smoothing out an UNK word for every tag, this allows for some probability to exist even if a new word arrives that has not been seen in training. However this does bring up an issue as sometimes non-sencical tag-word sequences may arise as a result of giving some probability for unkown words.
HMM with MLE: For mle the prediction for a given tag has a pro in that it is biased towards words scene in training/