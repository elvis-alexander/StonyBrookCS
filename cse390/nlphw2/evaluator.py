from sys import argv
import os

if __name__ == '__main__':
    if not os.path.exists("evaluator"):
        os.makedirs("evaluator")
    # maps tag to number of correct
    tag_to_correct = dict()
    # maps each prediction to an occurrence
    tag_predictions = dict()
    tag_count = dict()
    total_predictions = 0
    num_correct = 0
    # python evaluator.py results.txt
    f = open(argv[1], 'r')
    out = open(argv[2], 'w+')
    for line in f.readlines():
        tokens = line.split(" ")
        # disregard the first number token
        for token in tokens[1:len(tokens)]:
            end_index = token.rfind('/')
            predicted_tok = token[end_index+1:len(token)]
            token = token[0:end_index]
            end_index = token.rfind('/')
            actual_tok = token[end_index+1:len(token)]
            word = token[0:end_index]
            tag_predictions[predicted_tok] = tag_predictions.get(predicted_tok, 0) + 1
            tag_count[actual_tok] = tag_count.get(actual_tok, 0) + 1
            if actual_tok == predicted_tok:
                tag_to_correct[actual_tok] = tag_to_correct.get(actual_tok, 0) + 1
                num_correct += 1
            total_predictions += 1

    out.write("Accuracy: # correct predictions / # predictions: {}\n\n".format(float(num_correct) / total_predictions))

    for tag in tag_to_correct:
        correct_predictions = tag_to_correct.get(tag)
        total_tag_predicts = tag_predictions.get(tag)
        precision = (float(correct_predictions) / total_tag_predicts)
        num_tags = float(tag_count.get(tag, 0))
        recall = float(correct_predictions) / num_tags
        # Precision for tag k (P): # system correctly predicts tag k / # system predicts tag k
        out.write("Precision of tag {}: {}\n".format(tag, precision))
        # Recall for tag k (R): # system correctly predicts tag k / # tag k in the test set.
        out.write("Recall of tag {}: {}\n".format(tag, recall))
        f1 = (2 * precision * recall) / (precision + recall)
        out.write("F1 of tag {}: {}\n\n".format(tag, f1))
    f.close()
    out.close()