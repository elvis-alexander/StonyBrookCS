import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Actor {
    // The name of the Actor.
    private String name;
    // List of movies that the Actor is or has been in.
    // OPTIONAL: You may use List<Movie> as the data type, whichever is easier.
    private List<Movie> movies;
    // List of Actors that share movies with the current Actor.
    // OPTIONAL: You may use List<Actor> as the data type, whichever is easier.
    private List<Actor> friends;
    // Whether or not the Actor has been visited in the traversal.
    // This variable along with the path variable should only be modified when finding shortest path and the breadth first traversal.
    private boolean visited;
    // The current path up to the current Actor in the given traversal.
    // Note: This variable’s value will be different depending on where the traversal started from.
    private LinkedList<String> path;

    /**
     *
     * @param name
     */
    public Actor(String name) {
        this.name = name;
        this.movies = new ArrayList<>();
        this.friends = new ArrayList<>();
        this.path = new LinkedList<>();
    }

    /**
     *
     * @param potentialFriend
     * @return
     */
    public boolean areFriends(Actor potentialFriend) {
        // disallow self friend-ships
        if(this == potentialFriend)
            return true;
        return friends.contains(potentialFriend);
    }

    /**
     *
     * @param friend
     */
    public void addFriend(Actor friend) {
        // verify friendship doesnt already exist
        if(this.areFriends(friend))
            return;
        // add new friend
        this.friends.add(friend);
    }

    /**
     *
     * @param movie
     */
    public void addMovie(Movie movie) {
        // verify this user doesn't already have this movie in his list
        if(this.movies.contains(movie))
            return;
        // add new movie
        this.movies.add(movie);
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public List<Movie> getMovies() {
        return movies;
    }

    /**
     *
     * @param movies
     */
    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    /**
     *
     * @return
     */
    public List<Actor> getFriends() {
        return friends;
    }

    /**
     *
     * @param friends
     */
    public void setFriends(List<Actor> friends) {
        this.friends = friends;
    }

    /**
     *
     * @return
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     *
     * @param visited
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     *
     * @return
     */
    public LinkedList<String> getPath() {
        return path;
    }

    /**
     *
     * @param path
     */
    public void setPath(LinkedList<String> path) {
        this.path = path;
    }

    @Override
    public String toString() {
        //Actor: Jeff Bridges Friends: John Goodman, Julianne Moore, Steve Buscemi, Kevin Spacey, Mary McCormack, Alfre Woodard
        // Movies: The Big Lebowski(1998), K-PAX(2001)
        String s = "Name = " + name + " Friends: ";
        for(Actor f: friends) {
            s += f.getName() + ", ";
        }
        s += " Movies: ";
        for(Movie m: movies) {
            s += m.getTitle() + ", ";
        }
        return s;

    }
}
