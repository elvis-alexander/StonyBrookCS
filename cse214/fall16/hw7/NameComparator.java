
import java.util.Comparator;

public class NameComparator implements Comparator<Actor>{
    @Override
    public int compare(Actor o1, Actor o2) {
        return o1.getName().compareToIgnoreCase(o2.getName());
    }
}
