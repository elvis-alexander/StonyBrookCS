

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Classroom implements Serializable {
    private boolean hasWhiteboard;
    private boolean hasChalkBoard;
    private int numSeats;
    private List<String> AVEquipmentList;

    public Classroom(boolean hasWhiteboard, boolean hasChalkBoard, int numSeats, String a) {

        this.hasWhiteboard = hasWhiteboard;
        this.hasChalkBoard = hasChalkBoard;
        this.numSeats = numSeats;
        this.AVEquipmentList = new ArrayList<>(Arrays.asList(a.split(",")));
    }

    public boolean isHasWhiteboard() {
        return hasWhiteboard;
    }

    public void setHasWhiteboard(boolean hasWhiteboard) {
        this.hasWhiteboard = hasWhiteboard;
    }

    public boolean isHasChalkBoard() {
        return hasChalkBoard;
    }

    public void setHasChalkBoard(boolean hasChalkBoard) {
        this.hasChalkBoard = hasChalkBoard;
    }

    public int getNumSeats() {
        return numSeats;
    }

    public void setNumSeats(int numSeats) {
        this.numSeats = numSeats;
    }

    public List<String> getAVEquipmentList() {
        return AVEquipmentList;
    }

    public void setAVEquipmentList(List<String> AVEquipmentList) {
        this.AVEquipmentList = AVEquipmentList;
    }
}
