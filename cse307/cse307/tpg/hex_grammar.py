import tpg


class Node(object):
    """
    A base class for Node
    """
    def evaluate(self):
        """
        Called on the children of Node to evaluate that child
        """
        raise Exception("Not Implemented")


class HexNumber(Node):
    """
    A node representing a hex number
    """
    def __init__(self, h):
        self.hex = int(h, 16)

    # evaluate
    def evaluate(self):
        return self.hex


class Parser(tpg.Parser):
    """
    separator space '\s';
    token hex_start '[x | X]' str;
    token digit '([0-9 | a-e | A-E][0-9]*)' HexNumber;

    START/s -> HexStart/s HexNumber/s;
    HexStart/h -> hex_start/h;
    HexNumber/n -> (digit/n);
    """


parse = Parser()
while 1:
    l = raw_input("\n:")
    if l:
        try:
            print(parse(l).evaluate())
        except Exception:
            print(tpg.exc())
    else:
        break
