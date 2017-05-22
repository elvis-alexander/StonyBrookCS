
public class TripPlanner {

    public static void main(String[] args) {

        Itinerary i = new Itinerary();

        // [C,->B,A]
        // [->C,B,A]
        // [->B, A]
        // [B, ->A]
        // [C,B,->A]
        try {
            i.appendToTail(new TripStop("E"));
            i.appendToTail(new TripStop("D"));
            i.appendToTail(new TripStop("C"));
            i.appendToTail(new TripStop("B"));
            i.appendToTail(new TripStop("A"));

            i.cursorForward();
            i.cursorForward();
            i.display();
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
