import java.util.ArrayList;
import java.util.List;

public class Movie {

    // The title of the Movie.
    private String title;
    // List of Actors that are in the current Movie.
    // OPTIONAL: You may use List<Actor> as the data type, whichever is easier.
    private List<Actor> actors;
    // Year the movie was created.public Movie(String title)
    private int year;


    // Loads (using BigData) a movie using the passed title to create the URL and makes a new Movie object from it.

    /**
     *
     * @param title
     */
    public Movie(String title) {
        this.title = title;
        this.actors = new ArrayList<>();
    }

    /**
     * adds an actor
     * @param a
     */
    public void addActor(Actor a) {
        // verify actor isn't already is actors list
        if(actors.contains(a))
           return;
        this.actors.add(a);
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     */
    public List<Actor> getActors() {
        return actors;
    }

    /**
     *
     * @param actors
     */
    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    /**
     *
     * @return
     */
    public int getYear() {
        return year;
    }

    /**
     *
     * @param year
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * verifies all actors in production are friends
     */
    public void addActorFriends() {
        for (Actor actor : actors) {
            actor.addMovie(this);
            for (Actor friend : actors) {
                actor.addFriend(friend);
            }
        }
    }
}
