import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class SlideShowManager {
    // helper to get user input
    private static Scanner scanner = new Scanner(System.in);
    // represents commands run
    private static UndoRedoStack undoStack = new UndoRedoStack();
    // ctrl-z cmds
    private static UndoRedoStack redoStack = new UndoRedoStack();
    // view for user
    private static ArrayList<String> slideShow = new ArrayList<>();

    public static void main(String[] args) {
        init();
    }

    private static void init() {
        System.out.println("Welcome to Slideshow Manager!");
        boolean run = true;
        while (run) {
            printMenu();
            char user = getInput();
            switch (user) {
                case 'A':
                    addPhoto();
                    break;
                case 'R':
                    removePhoto();
                    break;
                case 'Y':
                    redo();
                    break;
                case 'Z':
                    undo();
                    break;
                case 'P':
                    printSlideShow();
                    break;
                case 'S':
                    swapPhotos();
                    break;
                case 'M':
                    movePhotos();
                    break;
                case 'Q':
                    System.out.println("Thank you, and goodbye!");
                    run = false;
                    break;
            }
        }
    }

    private static void redo() {
        if(redoStack.isEmpty()) {
            System.out.println("no commands to redo");
            return;
        }
        ActionCommand lastCmd = redoStack.pop();
        undoStack.push(lastCmd);
        lastCmd.perform(slideShow);
        System.out.println(lastCmd);
    }

    private static void undo() {
        if(undoStack.isEmpty()) {
            System.out.println("No commands to be undone");
            return;
        }
        ActionCommand lastCmd = undoStack.pop();
        redoStack.push(lastCmd);
        ActionCommand opposite = lastCmd.getInverse();
        opposite.perform(slideShow);
        System.out.println(opposite);
    }

    /**
     * moves photoes helper
     */
    private static void movePhotos() {
        System.out.print("Please enter source position: ");
        int src = Integer.parseInt(scanner.nextLine()) - 1;
        System.out.print("Please enter destination position: ");
        int dst = Integer.parseInt(scanner.nextLine()) - 1;
        // do swap
        String slide = slideShow.remove(src);
        slideShow.add(dst, slide);
        // save cmd
        ActionCommand moveCmd = new ActionCommand(src, dst, "", ActionType.MOVE);
        undoStack.push(moveCmd);
    }

    /**
     * swaps photos helper
     */
    private static void swapPhotos() {
        System.out.print("Please enter the first position: ");
        int firstPos = Integer.parseInt(scanner.nextLine()) - 1;
        System.out.print("Please enter the second position: ");
        int secondPos = Integer.parseInt(scanner.nextLine()) - 1;
        ActionCommand swapCmd = new ActionCommand(firstPos, secondPos, "", ActionType.SWAP);
        undoStack.add(swapCmd);
        Collections.swap(slideShow, firstPos, secondPos);
    }

    /**
     * prints slide show helper
     */
    private static void printSlideShow() {
        // print slideshow
        System.out.println("Slideshow:\n");
        System.out.println("---------------------------------------------------------------------------");
        for(int i = 0; i < slideShow.size(); ++i) {
            String slide = slideShow.get(i);
            System.out.print((i + 1) + ". " + slide + ", ");
        }
        System.out.println();
        // print undo stack
        System.out.println("Undo Stack:\n");
        System.out.println(undoStack);
        // print redo stack
        System.out.println("\nRedo Stack:\n");
        System.out.println(redoStack);
    }

    /**
     * removes photo helper
     */
    private static void removePhoto() {
        System.out.print("Please enter the position: ");
        int pos = Integer.parseInt(scanner.nextLine()) - 1;
        String s = slideShow.get(pos);
        ActionCommand removeCmd = new ActionCommand(pos, -1, s, ActionType.REMOVE);
        undoStack.push(removeCmd);
        slideShow.remove(pos);
    }

    /**
     * adds photo helper
     */
    private static void addPhoto() {
        System.out.print("Please enter the photo name: ");
        String photoName = scanner.nextLine();
        System.out.print("Please enter the position: ");
        int pos = Integer.parseInt(scanner.nextLine()) - 1;
        ActionCommand newCommand = new ActionCommand(pos, -1, photoName, ActionType.ADD);
        undoStack.push(newCommand);
        slideShow.add(pos, photoName);
    }

    /**
     * helper to get char to upper case from user
     * @return
     */
    private static char getInput() {
        System.out.print("Please select an option: ");
        String s = scanner.nextLine();
        if(s.length() > 0)
            return s.toUpperCase().charAt(0);
        return '.';
    }

    /**
     * nice template to print menue
     */
    private static void printMenu() {
        System.out.println("Menu:\n" +
                "     A) Add a photo\n" +
                "     R) Remove a photo\n" +
                "     S) Swap Photos\n" +
                "     M) Move Photo\n" +
                "     P) Print\n" +
                "     Z) Undo\n" +
                "     Y) Redo\n" +
                "     Q) Quit");
    }

}
