import sys
import itertools, collections
import tree

try:
    _, parsefilename, goldfilename = sys.argv
except:
    sys.stderr.write("usage: evalb.py <parse-file> <gold-file>\n")
    sys.exit(1)

def _brackets_helper(node, i, result):
    i0 = i
    if len(node.children) > 0:
        for child in node.children:
            i = _brackets_helper(child, i, result)
        j0 = i
        if len(node.children[0].children) > 0: # don't count preterminals
            result[node.label, i0, j0] += 1
    else:
        j0 = i0 + 1
    return j0

def brackets(t):
    result = collections.defaultdict(int)
    _brackets_helper(t.root, 0, result)
    return result

matchcount = parsecount = goldcount = 0

for parseline, goldline in itertools.izip(open(parsefilename), open(goldfilename)):
    gold = tree.Tree.from_str(goldline)
    goldbrackets = brackets(gold)
    goldcount += len(goldbrackets)

    if parseline.strip() == "0":
        continue
    
    parse = tree.Tree.from_str(parseline)
    parsebrackets = brackets(parse)
    parsecount += len(parsebrackets)

    for bracket,count in parsebrackets.iteritems():
        matchcount += min(count,goldbrackets[bracket])

print "%s\t%d brackets" % (parsefilename, parsecount)
print "%s\t%d brackets" % (goldfilename, goldcount)
print "matching\t%d brackets" % matchcount

