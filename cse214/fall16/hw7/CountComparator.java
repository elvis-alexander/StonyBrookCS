
import java.util.Comparator;

public class CountComparator implements Comparator<Actor> {
    public int compare(Actor o1, Actor o2) {
        if(o1.getCount() == o2.getCount())
            return 0;
        return o1.getCount() > o2.getCount() ? 1 : -1;
    }
}
