import java.util.ArrayList;
import java.util.List;
public class Movie {
    private String title;   // title of the movie
    private int year;       // year that the movie was released
    private List<Actor> actors;

    public Movie(String t, int y) {
        this.title = t;
        this.year = y;
        this.actors = new ArrayList<>();
    }

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public int getYear() {return year;}
    public void setYear(int year) {this.year = year;}

    public List<Actor> getActors() {return actors;}
    public void setActors(List<Actor> actors) {this.actors = actors;}

    public String actorsToString() {
        StringBuffer b = new StringBuffer();
        for(Actor a : actors) {
            b.append(a.getName() + ", ");
        }
        return b.toString();
    }
}
