
import java.util.Comparator;

public class TitleComparator implements Comparator<Movie> {
    @Override
    public int compare(Movie o1, Movie o2) {
        return o1.getTitle().compareToIgnoreCase(o2.getTitle());
    }
}
