
public class TripStopNode {
    private TripStop data;
    private TripStopNode next;
    private TripStopNode prev;

    public TripStopNode(TripStop d) throws IllegalArgumentException {
        if(d == null) {
            throw new IllegalArgumentException("Trip Stop must be instantiated");
        }
        this.data = d;
        this.next = null;
        this.prev = null;
    }

    public TripStop getData() {
        return data;
    }

    public void setData(TripStop data) throws IllegalArgumentException {
        if(data == null) {
            throw new IllegalArgumentException("Trip Stop must be instantiated");
        }
        this.data = data;
    }

    public TripStopNode getNext() {
        return next;
    }

    public void setNext(TripStopNode next) {
        this.next = next;
    }

    public TripStopNode getPrev() {
        return prev;
    }

    public void setPrev(TripStopNode prev) {
        this.prev = prev;
    }
}
