import java.util.Arrays;
import java.util.List;

public class TreeNode {
    // this holds a message only if it is a leaf
    // otherwise if it is ! a lead it hold words to trigger going down the path
    private List<String> keywords;
    private TreeNode left;
    private TreeNode right;

    public TreeNode(List<String> k) {
        this.keywords = k;
    }

    public TreeNode(String s) { this.keywords = Arrays.asList(s.split(","));}

    // function that returns true if the node is a leaf and its left and right subtrees are null, otherwise false.
    public boolean isLeaf() {
        return left == null && right == null;
    }

    public void setLeft(TreeNode l) {this.left = l;}
    public TreeNode getLeft() {return left;}

    public void setRight(TreeNode r) {this.right = r;}
    public TreeNode getRight() { return right;}

    public List<String> getKeywords(){return keywords;}
    public void setKeywords(List<String> k){this.keywords = k;}

    public String display() {
        StringBuffer b = new StringBuffer();
        if(keywords.size() == 1) {
            b.append(keywords.get(0));
            return b.toString();
        }
        int i = 0;
        for(i = 0; i < keywords.size() - 1; ++i) {
            b.append(keywords.get(i) + ",");
        }
        b.append(keywords.get(i));
        return b.toString();
    }

    public boolean containsWord(String s) {return keywords.contains(s);}

    public void displayKeyWords() {
        if(keywords.size() == 1) {
            System.out.println(keywords.get(0));
            return;
        }
        for(String s : keywords) {
            System.out.print(s + ", ");
        }
        System.out.println();
    }

    public String firstKey() {
        return keywords.get(0);
    }
}