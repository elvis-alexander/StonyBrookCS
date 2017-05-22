import tpg

# these are the nodes of the abstract syntax tree.
class Node(object):
    """
    A base class for nodes. Can be used to superclass different nodes
    """
    def evaluate(self):
        """
            Called on children of Node to evaluate that child.
        """
        raise Exception("Not Implemented")


# represents an integer.
class IntLiteral(Node):
    """
    A node representing integer literals
    """
    def __init__(self, v):
        self.value = v;

    def evaluate(self):
        return self.value


# represents a real literal
class RealLiteral(Node):
    """
    A node representing real literal
    """
    def __init__(self, v):
        self.value = v

    def evaluate(self):
        return self.value


class RealLiteral(Node):
    """
    A node representing real literal
    """
    def __init__(self, v):
        self.value = v

    def evaluate(self):
        return self.value




class Parser(tpg.Parser):
    """
    separator space '\s';
    token int '[0-9]+' IntLiteral;
    token real '([0-9]()([0-9]*))' RealLiteral;
    token str '' StringLiteral


    """