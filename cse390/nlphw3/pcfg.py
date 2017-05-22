from tree import Tree

# constructs pcfg.txt file containing
# all grammars with their given probabilities
if __name__ == '__main__':
    train = open('train.trees')
    out = open('grammar.txt', 'w+')
    unary_rules = dict()
    binary_rules = dict()
    non_terminals = dict()
    # train
    for line in train.readlines():
        t = Tree.from_str(line)
        r = t.root
        t.traverse(r, unary_rules, binary_rules, non_terminals)
    # construct pcfg document
    for key in sorted(binary_rules.keys()):
        alpha = key[0]
        beta_one = key[1]
        beta_two = key[2]
        grammar_occurrences = binary_rules[(alpha, beta_one, beta_two)]
        alpha_count = non_terminals[alpha]
        mle = float(grammar_occurrences) / alpha_count
        output = "{} {} {} {}\n".format(alpha, beta_one, beta_two, mle)
        out.write(output)
    # print unary_rules
    # for key in sorted(unary_rules):
    #     alpha = key[0]
    #     beta = key[1]
    #     grammar_occurrences = unary_rules[(alpha, beta)]
    #     alpha_count = non_terminals[alpha]
    #     laplace = float(grammar_occurrences + 1) / float(alpha_count + len(unary_rules) + 1)
    #     output = "{} {} {}\n".format(alpha, beta, laplace)
    #     out.write(output)
    # clean up
    train.close()
    out.close()