# tree.py
# David Chiang <chiang@isi.edu>
# slightly modified by Liang Huang <lhuang@isi.edu> to remove quotes surrounding words.

import re

class RootDeletedException(Exception):
    pass

class Node(object):
    def __init__(self, label, children):
        self.label = label
        self.children = children
        for (i,child) in enumerate(self.children):
            if child.parent is not None:
                child.detach()
            child.parent = self
            child.order = i
        self.parent = None
        self.order = 0

    def __str__(self):
        return self.label

    def _subtree_str(self):
        if len(self.children) != 0:
            return "%s(%s)" % (self.label, " ".join(child._subtree_str() for child in self.children))
        else:
            s = '"%s"' % self.label
            #s = s.replace("(", "-LRB-")
            #s = s.replace(")", "-RRB-")
            return s

    def insert_child(self, i, child):
        if child.parent is not None:
            child.detach()
        child.parent = self
        self.children[i:i] = [child]
        for j in range(i,len(self.children)):
            self.children[j].order = j

    def append_child(self, child):
        if child.parent is not None:
            child.detach()
        child.parent = self
        self.children.append(child)
        child.order = len(self.children)-1

    def delete_child(self, i):
        self.children[i].parent = None
        self.children[i].order = 0
        self.children[i:i+1] = []
        for j in range(i,len(self.children)):
            self.children[j].order = j

    def detach(self):
        if self.parent is None:
            raise RootDeletedException
        self.parent.delete_child(self.order)

    def delete_clean(self):
        "Cleans up childless ancestors"
        parent = self.parent
        self.detach()
        if len(parent.children) == 0:
            parent.delete_clean()

    def bottomup(self):
        for child in self.children:
            for node in child.bottomup():
                yield node
        yield self

    def leaves(self):
        if len(self.children) == 0:
            yield self
        else:
            for child in self.children:
                for leaf in child.leaves():
                    yield leaf

    # returns True if node (grammar is a unary rule) False ow
    def is_unary(self):
        return len(self.children) == 1

    # returns True if node (grammar is a binary rule) False ow
    def is_binary(self):
        return len(self.children) == 2

class Tree(object):
    def __init__(self, root):
        self.root = root

    def __str__(self):
        return self.root._subtree_str()

    interior_node = re.compile(r"\s*([^\s(]*)\(")
    close_brace = re.compile(r"\s*\)")
    # lhuang: unquote
    leaf_node = re.compile(r'\s*([^\s)]+)')

    @staticmethod
    def _scan_tree(s):
        result = Tree.interior_node.match(s)
        if result != None:
            label = result.group(1)
            pos = result.end()
            children = []
            (child, length) = Tree._scan_tree(s[pos:])
            while child != None:
                children.append(child)
                pos += length
                (child, length) = Tree._scan_tree(s[pos:])
            result = Tree.close_brace.match(s[pos:])
            if result != None:
                pos += result.end()
                return Node(label, children), pos
            else:
                return (None, 0)
        else:
            result = Tree.leaf_node.match(s)
            if result != None:
                pos = result.end()
                label = result.group(1)
                #label = label.replace("-LRB-", "(")
                #label = label.replace("-RRB-", ")")
                return (Node(label,[]), pos)
            else:
                return (None, 0)

    @staticmethod
    def from_str(s):
        s = s.strip()
        (tree, n) = Tree._scan_tree(s)
        return Tree(tree)

    def bottomup(self):
        return self.root.bottomup()

    def leaves(self):
        return self.root.leaves()

    def remove_empty(self):
        nodes = list(self.bottomup())
        for node in nodes:
            if node.label in ["-NONE-", "XXX"]:
                try:
                    node.delete_clean()
                except RootDeletedException:
                    self.root = None

    def remove_unit(self):
        nodes = list(self.bottomup())
        for node in nodes:
            if len(node.children) == 1:
                child = node.children[0]
                if len(child.children) > 0:
                    node.label = "%s_%s" % (node.label, child.label)
                    child.detach()
                    for grandchild in list(child.children):
                        node.append_child(grandchild)

    def binarize_right(self):
        nodes = list(self.bottomup())
        for node in nodes:
            if len(node.children) > 2:
                # create a right-branching structure
                children = list(node.children)
                children.reverse()
                vlabel = node.label+"*"
                prev = children[0]
                for child in children[1:-1]:
                    prev = Node(vlabel, [child, prev])
                node.append_child(prev)

    def binarize_left(self):
        nodes = list(self.bottomup())
        for node in nodes:
            if len(node.children) > 2:
                # create a left-branching structure
                vlabel = node.label+"*"
                children = list(node.children)
                prev = children[0]
                for child in children[1:-1]:
                    prev = Node(vlabel, [prev, child])
                node.insert_child(0, prev)

    def binarize(self):
        nodes = list(self.bottomup())
        for node in nodes:
            if len(node.children) > 2:

                if node.label in ['SQ']:
                    # create a right-branching structure
                    children = list(node.children)
                    children.reverse()
                    vlabel = node.label+"*"
                    prev = children[0]
                    for child in children[1:-1]:
                        prev = Node(vlabel, [child, prev])
                    node.append_child(prev)
                else:
                    # create a left-branching structure
                    vlabel = node.label+"*"
                    children = list(node.children)
                    prev = children[0]
                    for child in children[1:-1]:
                        prev = Node(vlabel, [prev, child])
                    node.insert_child(0, prev)

    def is_leaf(self, n):
        return True if len(n.children) == 0 else False

    def foo(self):
        i = 1

    # unary/binary maps rules to occurences, non terminals maps non terminals to occurrences
    def traverse(self, r, unary, binary, non_terminals):
        if self.is_leaf(r):
            return
        non_terminals[r.label] = non_terminals.get(r.label, 0) + 1
        left = r.children[0]
        self.traverse(left, unary, binary, non_terminals)
        if r.is_unary():
            unary[(r.label, left.label)] = unary.get((r.label, left.label), 0) + 1
        elif r.is_binary():
            right = r.children[1]
            self.traverse(right, unary, binary, non_terminals)
            binary[(r.label, left.label, right.label)] = binary.get((r.label, left.label, right.label), 0) + 1


# if __name__ == "__main__":
# import sys
# for line in sys.stdin:
#     t = Tree.from_str(line)
#     print t


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