class SemanticError(Exception):
    """
    This is the class of the exception that os raised when a semantic error occurs
    """


# these are the nodes of the abstract syntax tree
class Node(object):
    """
    A base class for nodes. Can be used to superclass different nodes
    """
    def evaluate(self):
        """
        Called on children of Node to evaluate that child
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


# represents a comparison op
class ComparisonLiteral(Node):
    """
    A node representing the following comparators: <,<=,==,<>,>,>=,=
    """
    def __init__(self, v):
        self.value = v

    def evaluate(self):
        return self.value


# represents a boolean operation
class BooleanLiteral(Node):
    """
    A node representing the a boolean literal: and,or,not
    """
    def __init__(self, v):
        self.value = v

    def evaluate(self):
        return self.value

# a and b must be integers. the two integers are compared <,<=,==,<>,>,>=.
class Comparison(Node):
    """
    A node representing the operator(comparison) between two operands(integers)
    """
    def __init__(self, op1,op2, o):
        self.operand1 = op1
        self.operand2 = op2
        self.operator = o

    def evaluate(self):
        operand1 = self.operand1.evaluate()
        operand2 = self.operand2.evaluate()
        operator = self.operator.evaluate()

        if not isinstance(operand1, int):
            raise SemanticError()
        if not isinstance(operand2, int):
            raise SemanticError()
        # if not isinstance(operator, str):
        # raise SemanticError()

        if operator == '<':
            return operand1 < operand2
        elif operator == '<=':
            return operand1 <= operand2
        elif operator == '==':
            return operand1 == operand2
        elif operator == '<>':
            return operand1 != operand2
        elif operator == '>':
            return operand1 > operand2
        elif operator == '>=':
            return operand1 >= operand2


#
class Bool(Node):
    """
    Boolean AND, OR, NOT: A and if present, B must be integers.  If the integer is 0 then it is considered
    false.  All other integers are considered true.  The boolean operation is performed.
    If its true result of the expression, otherwise it is the integer 0.
    """
    def __init__(self, n1, n2, bo):
        self.numone = n1
        self.numtwo = n2
        self.boolop = bo

    def evaluate(self):
        numone = self.numone.evaluate()
        numtwo = self.numone.evaluate()
        boolop = self.numone.evaluate()

        if not isinstance(numone, int):
            raise SemanticError()
        if not isinstance(numtwo, int):
            raise SemanticError()
        # if not isinstance(boolop, str):
        # raise SemanticError()

        if boolop == 'and':
            return numone and numtwo
        elif boolop == 'or':
            return numone or numtwo


def main():
    b = Bool(1)



main()

