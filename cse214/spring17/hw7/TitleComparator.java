import java.util.Comparator;

public class TitleComparator implements Comparator<Movie> {
    /**
     * Comparator for movies
     * @param o1
     * @param o2
     * @return
     */
    @Override
    public int compare(Movie o1, Movie o2) {
        return o1.getTitle().compareTo(o2.getTitle());
    }
}
