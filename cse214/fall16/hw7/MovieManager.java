
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MovieManager {
    private List<Movie> movies;
    private List<Actor> actors;

    public MovieManager() {
        this.movies = new ArrayList<>();
        this.actors = new ArrayList<>();
    }

    public MovieManager(String url) {
    }

    // returns true if this actor is already in the list
    public Actor getSpecifiedActor(String actorName) {
        for(int i = 0; i < actors.size(); ++i) {
            Actor a = actors.get(i);
            if(a.getName().equalsIgnoreCase(actorName))
                return a;
        }
        return null;
    }

    public int indexOfMovie(String movieName) {
        int i = 0;
        for(Movie m : movies) {
            if(m.getTitle().equalsIgnoreCase(movieName))
                return i;
            ++i;
        }
        return -1;
    }

    public void removeActor(String acName) {
        int i = indexOfActor(acName);
        if(i == -1)
            return;
        try {
            actors.remove(i);
        }catch (Exception e){}  // shouldnt occurr...
    }

    private int indexOfActor(String actorName) {
        int i = 0;
        for(Actor a : actors) {
            if(a.getName().equalsIgnoreCase(actorName))
                return i;
            ++i;
        }
        return -1;
    }

    public List<Movie> getMovies() {
        return this.movies;
    }
    public List<Actor> getActors() {
        return this.actors;
    }

    public List<Movie> getSortedMovies(Comparator c) {
        Collections.sort(movies, c);
        return this.movies;
    }
    public List<Actor> getSortedActors(Comparator c) {
        Collections.sort(actors, c);
        return this.actors;
    }

}
