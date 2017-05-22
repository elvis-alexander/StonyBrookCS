import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class ActorGraph {
    // maps actor names to actors
    private HashMap<String, Actor> actorsByName;
    // maps movie title to movie
    private HashMap<String, Movie> moviesByTitle;

    /**
     * No-Args Constructor
     */
    public ActorGraph() {
        this.actorsByName = new HashMap<>();
        this.moviesByTitle = new HashMap<>();
    }

    /**
     *
     * @param firstName
     * @return
     */
    public LinkedList<String> bfs(String firstName) {
        resetActors();
        LinkedList<String> out = new LinkedList<>();
        Actor srcActor = getActor(firstName);
        Queue<Actor> queue = new LinkedList<>();
        queue.add(srcActor);
        srcActor.setVisited(true);
        while (!queue.isEmpty()) {
            Actor curr = queue.poll();
            out.add(curr.getName());
            for(Actor friend: curr.getFriends()) {
                if(!friend.isVisited()) {
                    friend.setVisited(true);
                    queue.add(friend);
                }
            }
        }
        return out;
    }


    /**
     *
     * @param firstName
     * @param secondName
     * @return
     */
    public LinkedList<String> shortestPath(String firstName, String secondName) {
        resetActors();
        LinkedList<String> out = new LinkedList<>();
        Actor srcActor = getActor(firstName);
        Actor dstActor = getActor(secondName);
        Queue<Actor> queue = new LinkedList<>();
        queue.add(srcActor);
        srcActor.setVisited(true);
        while (!queue.isEmpty()) {
            Actor curr = queue.poll();
            out.add(curr.getName());
            for(Actor friend: curr.getFriends()) {
                if(friend == dstActor) {
                    out.add(friend.getName());
                    srcActor.setPath(out);
                    return out;
                }
                if(!friend.isVisited()) {
                    friend.setVisited(true);
                    queue.add(friend);
                }
            }
        }
        System.out.println("No path :(");
        return out;
    }

    /**
     * resets visited actors
     */
    private void resetActors() {
        for(Actor a: getActorsByName().values())
            a.setVisited(false);
    }

    /**
     * Adds a new actor to actorsByName hash table
     * if it does not exist
     * @param actorName
     */
    public void addActor(String actorName) {
        if(actorsByName.containsKey(actorName))
            return;
        Actor newActor = new Actor(actorName);
        actorsByName.put(actorName, newActor);
    }

    /**
     * Adds a new movie to moviesByTitle hashtable
     * if it does not exist
     * @param movieTitle
     */
    public void addMove(String movieTitle) {
        if(moviesByTitle.containsKey(movieTitle))
            return;
        Movie newMovie = new Movie(movieTitle);
        moviesByTitle.put(movieTitle, newMovie);
    }

    /**
     *
     * @param name
     * @return
     */
    public Actor getActor(String name) {
        if(!actorsByName.containsKey(name))
            return null;
        return actorsByName.get(name);
    }

    /**
     *
     * @param title
     * @return
     */
    public Movie getMovie(String title) {
        if(!moviesByTitle.containsKey(title))
            return null;
        return moviesByTitle.get(title);
    }

    /**
     *
     * @param title
     * @param actors
     */
    public void addActorsToMovie(String title, String[] actors) {
        Movie movie = getMovie(title);
        for(String name: actors) {
            Actor actor = getActor(name);
            movie.addActor(actor);
        }
    }

    /**
     *
     * @return
     */
    public HashMap<String, Actor> getActorsByName() {
        return actorsByName;
    }

    /**
     *
     * @param actorsByName
     */
    public void setActorsByName(HashMap<String, Actor> actorsByName) {
        actorsByName = actorsByName;
    }

    /**
     *
     * @return
     */
    public HashMap<String, Movie> getMoviesByTitle() {
        return moviesByTitle;
    }

    /**
     *
     * @param moviesByTitle
     */
    public void setMoviesByTitle(HashMap<String, Movie> moviesByTitle) {
        moviesByTitle = moviesByTitle;
    }


}
