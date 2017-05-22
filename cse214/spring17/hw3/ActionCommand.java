import java.util.ArrayList;
import java.util.Collections;

public class ActionCommand {
    private int positionOne;
    // not going to be used for add or remove
    private int positionTwo;
    private String photo;
    private final ActionType type;

    /**
     * constructor
     * @param positionOne
     * @param positionTwo
     * @param photo
     * @param type
     */
    public ActionCommand(int positionOne, int positionTwo, String photo, ActionType type) {
        this.positionOne = positionOne;
        this.positionTwo = positionTwo;
        this.photo = photo;
        this.type = type;
    }

    /**
     * performs the action on the given slideshow (ArrayList of String) object.
     * @param slideshow
     */
    public void perform(ArrayList<String> slideshow) {
        switch (this.type) {
            case ADD:
                slideshow.add(this.positionOne, this.photo);
                break;
            case REMOVE:
                slideshow.remove(this.positionOne);
                break;
            case MOVE:
                slideshow.remove(this.positionOne);
                slideshow.add(this.positionTwo, this.photo);
                break;
            case SWAP:
                Collections.swap(slideshow, this.positionOne, this.positionTwo);
                break;
        }
    }

    /**
     * logical inverse of a command
     * @return
     */
    public ActionCommand getInverse() {
        switch (this.type) {
            case ADD:
                return new ActionCommand(this.positionOne, -1, this.photo, ActionType.REMOVE);
            case REMOVE:
                return new ActionCommand(this.positionOne, -1, this.photo, ActionType.ADD);
            case MOVE:
                return new ActionCommand(this.positionTwo, this.positionOne, this.photo, ActionType.REMOVE);
            case SWAP:
                return new ActionCommand(this.positionTwo, this.positionOne, this.photo, ActionType.SWAP);
        }
        return null;
    }

    /**
     * toString for an action committed by user
     * @return
     */
    public String toString() {
        switch (this.type) {
            case ADD:
                return String.format("ADD %s in position %d\n", this.photo, this.positionOne + 1);
            case REMOVE:
                return String.format("REMOVE %s from position %d\n", this.photo, this.positionOne + 1);
            case MOVE:
                return String.format("MOVE position %d to position %d\n", this.positionOne + 1, this.positionTwo + 1);
            case SWAP:
                return String.format("SWITCH position %d and position %d\n", this.positionOne + 1, this.positionTwo + 1);
        }
        return String.format("%s %s\n", type, photo);
    }

}
