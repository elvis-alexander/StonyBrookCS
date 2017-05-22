

public class Itinerary {
    private TripStopNode head;
    private TripStopNode tail;
    private TripStopNode cursor;
    private int num_stops;
    private int total_dist;

    public Itinerary() {
        this.head = null;
        this.cursor = null;
        this.tail = null;
    }


    public int getStopsCount() {
        return num_stops;
    }
    public int getTotalDist() {
        return total_dist;
    }

    public TripStop getCursorStop() {
        return cursor.getData();
    }

    public void resetCursorToHead() {
        this.cursor = head;
    }

    public void cursorForward() throws EndOfItineraryException {
        if(cursor == tail) {
            throw new EndOfItineraryException("Cursor is at the end of the list.");
        }
        cursor = cursor.getNext();
    }

    public void cursorBackward() throws EndOfItineraryException {
        if(cursor == head) {
            throw new EndOfItineraryException("Cursor is at the head of the list.");
        }
        cursor = cursor.getPrev();
    }


    public void appendToTail(TripStop newStop) {
        if (newStop == null) {
            throw new IllegalArgumentException("New Stop must be instantiated");
        }
        TripStopNode node = new TripStopNode(newStop);
        if (tail == null) {
            head = node;
            cursor = node;
        } else {
            tail.setNext(node);
            node.setPrev(tail);
        }
        tail = node;
    }

    

    public void insertBeforeCursor(TripStop newStop) throws IllegalArgumentException {
        if(newStop == null) {
            throw new IllegalArgumentException("New Stop must be instantiated");
        }
        TripStopNode node = new TripStopNode(newStop);
        if(cursor == null) {
            head = node;
            cursor = node;
            tail = node;
        } else {
            node.setPrev(cursor.getPrev());
            node.setNext(cursor);
            if(cursor.getPrev() != null) {
                cursor.getPrev().setNext(node);
            }
            cursor.setPrev(node);
            cursor = node;
            if(cursor.getPrev() == null) {
                head = cursor;
            }
        }
    }

    public void display() {
        TripStopNode tmp = head;
        while (tmp != null) {
            if(tmp == cursor) {
                System.out.print("->" + tmp.getData().getLocation() + " , ");
            } else {
                System.out.print(tmp.getData().getLocation() + " , ");
            }
            tmp = tmp.getNext();
        }
        System.out.println();
    }
}
