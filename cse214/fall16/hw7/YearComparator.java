
import java.util.Comparator;

public class YearComparator implements Comparator<Movie> {
    @Override
    public int compare(Movie o1, Movie o2) {
        if(o1.getYear() > o2.getYear())
            return 1;
        else if(o1.getYear() == o2.getYear())
            return 0;
        else
            return -1;
    }
}
