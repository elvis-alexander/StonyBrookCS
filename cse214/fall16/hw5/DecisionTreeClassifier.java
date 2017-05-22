
import java.util.Arrays;
import java.util.Scanner;

public class DecisionTreeClassifier {
    private static Scanner buffer = new Scanner(System.in);
    private static TreeNavigator tree = new TreeNavigator();

    public static void main(String[] args) {
        try {
            boolean run = true;
            while (run) {
                printMainMenu();
                char c = getInput();
                switch (c) {
                    case 'I':
                        importTree();
                        break;
                    case 'E':
                        editTree();
                        break;
                    case 'Q':
                        System.exit(0);
                        break;
                    default:
                        System.out.println("invalid option");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* imports tree from a file */
    public static void importTree() throws Exception {
        System.out.print("Please enter a filename: ");
        String f = buffer.nextLine();
        tree = TreeNavigator.buildTree(f);
        System.out.println("Tree Loaded.");
    }

    /* edit tree control flow */
    public static void editTree() {
        /*
        -> Classify (Sentence)
        -> Path (Sentence)
        -> Import (file)

        Cursor To Root (print cursor)
        Cursor Left (print cursor)
        Cursor right (print cursor)
        Edit Cursor (print cursor)
        Add Left Child (doesn't move cursor)
        Add Right Child (doesn't move cursor)*/
        if(tree.getCursor() == tree.getRoot()) {
            System.out.println("Cursor is at root");
        }
        System.out.print("Current node keywords: ");
        tree.getCursor().displayKeyWords();
        boolean editing = true;
        while(editing) {
            printEditTreeMenu();
            char c = getEditInput();
            switch(c) {
                case 'E':
                    editKeyWords();
                    break;
                case 'A':
                    addChildren();
                    break;
                case 'C':
                    addChildren();
                    break;
                case 'N':
                    cursorToNo();
                    break;
                case 'Y':
                    cursorToYes();
                    break;
                case 'R':
                    cursorToRoot();
                    break;
                case 'P':
                    cursorToParent();
                    break;
                case 'M':
                    return;
                default:
                    System.out.println("invalid option");
                    break;
            }

        }
    }

    /* edit cursors key words */
    public static void editKeyWords() {
        System.out.print("Please enter keywords for this node, seperated by a comma:");
        String k = buffer.nextLine();
        tree.editCursor(k);
        System.out.println("Keywords updated to: " + k);
    }

    public static void addChildren() {
        System.out.print("Please enter terminal text for the no leaf: ");
        String l = buffer.nextLine();
        System.out.print("Please enter terminal text for the yes leaf: ");
        String r = buffer.nextLine();
        TreeNode tl = new TreeNode(l);
        TreeNode tr = new TreeNode(r);
        tree.getCursor().setLeft(tl);
        tree.getCursor().setRight(tr);
        System.out.println(String.format("Children are: yes - '%s' and no - '%s'", r, l));
    }

    public static void cursorToNo() {
        tree.cursorLeft();
        tree.printCursor();
    }

    public static void cursorToYes() {
        tree.cursorRight();
        tree.printCursor();
    }

    public static void cursorToRoot() {
        tree.resetCursor();
        System.out.println("Cursor moved. Cursor is at root.");
        System.out.print("Current node keywords: ");
        tree.getCursor().displayKeyWords();
    }

    /* move cursor to parent */
    public static void cursorToParent() {
        TreeNode p = tree.getParent(tree.getRoot(), tree.getCursor());
        tree.setCursor(p);
        System.out.println("Cursor moved to parent.");
        tree.getCursor().displayKeyWords();
    }

    /* returns char */
    public static char getInput() {
        System.out.print("Please select an option: ");
        return buffer.nextLine().toUpperCase().charAt(0);
    }

    /* return char for edit menu */
    public static char getEditInput() {
        System.out.print("Please select an Edit option: ");
        return buffer.nextLine().toUpperCase().charAt(0);
    }

    /* prints menu I */
    public static void printMainMenu() {
        System.out.println("Menu:");
        System.out.println("\tI)Import a tree from a file");
        System.out.println("\tE)Edit current tree");
        System.out.println("\tC)Classify a Description");
        System.out.println("\tP)Show decision path for a Description");
        System.out.println("\tQ) Quit.");
    }

    /* prints edit tree menu */
    public static void printEditTreeMenu() {
        System.out.println("Please select an option:");
        System.out.println("\tE)Edit Keywords");
        System.out.println("\tC)Add Children ");
        System.out.println("\tD)Delete Children, and Make Leaf ");
        System.out.println("\tN)Cursor to No Child");
        System.out.println("\tY)Cursor to Yes Child");
        System.out.println("\tR)Cursor to Root");
        System.out.println("\tP)Cursor to Parent");
        System.out.println("\tM)Main Menu");
    }
}