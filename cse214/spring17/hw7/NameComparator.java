import java.util.Comparator;

public class NameComparator implements Comparator<Actor> {
    /**
     * Comparator for Actors
     * @param o1
     * @param o2
     * @return
     */
    @Override
    public int compare(Actor o1, Actor o2) {
        // sort by first name
        return o1.getName().compareTo(o2.getName());
    }
}
