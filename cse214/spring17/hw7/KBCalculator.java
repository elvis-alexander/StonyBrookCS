import java.util.*;

import big.data.DataSource;



public class KBCalculator {
    // used for input
    private static Scanner input = new Scanner(System.in);
    // main datasource
    private static ActorGraph graph = new ActorGraph();
    // config for api
    private static final String prefix = "http://www.omdbapi.com/?t=";
    private static final String postfix="&y=&plot=short&r=xml";
    // served as connector to open movie database api
    private static DataSource ds;

    public static void main(String[] args) {
        System.out.println("Welcome to the Kevin Bacon Calculator!");
        boolean run = true;
        while (run) {
            try {
                printOptions();
                char option = getOption();
                switch (option) {
                    case 'I':
                        importMovie();
                        break;
                    case 'A':
                        printAllActors();
                        break;
                    case 'M':
                        printAllMovies();
                        break;
                    case 'P':
                        printShortestPath();
                        break;
                    case 'B':
                        bfs();
                        break;
                    case 'L':
                        lookUp();
                        break;
                    case 'Q':
                        run = false;
                        System.out.println("Goodbye");
                        break;

                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Uhoh looks like something went wrong " + e.getMessage());
            }
        }

    }

    private static void lookUp() {
        System.out.print("Enter a name: ");
        String name = input.nextLine();
        Actor actor = graph.getActor(name);
        System.out.println(actor);
    }

    private static void bfs() {
        System.out.print("Enter a starting actor: ");
        String src = input.nextLine();
        LinkedList<String> bfsOutput = graph.bfs(src);
        for(String actor: bfsOutput) {
            System.out.print(actor + ", ");
        }
        System.out.println();

    }

    private static void printShortestPath() {
        System.out.print("Please enter the first name: ");
        String firstName = input.nextLine();
        System.out.print("Please enter the second name: ");
        String secondName = input.nextLine();
        LinkedList<String> actorNames = graph.shortestPath(firstName, secondName);

        if(actorNames.size() == 0) {
            System.out.println("No path");
        } else if(actorNames.get(actorNames.size() - 1).equals(secondName)) {
            for (String name : actorNames) {
                System.out.print(name + ", ");
            }
            System.out.println();
        } else {
            System.out.println("No path");
        }
    }

    private static void printOptions() {
        System.out.println("Options:\n" +
                "        I) Import a Movie\n" +
                "        A) Print all actors\n" +
                "        M) Print all movies\n" +
                "        P) Print the shortest path between two actors.\n" +
                "        B) Print the BFS (Breadth First Search) from a given actor\n" +
                "        L) Lookup Actor By Name\n" +
                "        Q) Quit");
    }

    private static char getOption() {
        System.out.print("Please enter an option: ");
        String s = input.nextLine();
        if(s == null || s.length() < 1 || s.equals(""))
            return 'z';
        return s.toUpperCase().charAt(0);
    }

    private static void importMovie() {
        System.out.print("Enter the movie title: ");
        // fetch title
        String title = input.nextLine();
        // fetch data from api
        ds = DataSource.connectXML(prefix+title.replace(' ','+')+postfix);
        ds.load();
        String actualTitle = ds.fetchString("movie/title");
        String[] actors = ds.fetchString("movie/actors").split(", ");
        int year = ds.fetchInt("movie/year");
        // add all actors if necessary
        for(String name: actors) {
            graph.addActor(name);
        }
        // add new movie, if necessary
        graph.addMove(actualTitle);
        // add actos to movies
        graph.addActorsToMovie(actualTitle, actors);
        Movie movie = graph.getMovie(actualTitle);
        movie.addActorFriends();
        System.out.println(String.format("%s (%d) Starring: %s", actualTitle, year, Arrays.toString(actors)));
    }

    private static void printAllActors() {
        System.out.println("Here is the alphabetic list of actors:");
        System.out.println("-----------------------------------------------");
        Collection<Actor> coll = graph.getActorsByName().values();
        List<Actor> actors = new ArrayList<>(coll);
        Collections.sort(actors, new NameComparator());
        for(Actor actor: actors) {
            System.out.println(actor.getName());
        }
    }

    private static void printAllMovies() {
        System.out.println("Here is the alphabetic list of movies:");
        System.out.println("-----------------------------------------------");
        Collection<Movie> coll = graph.getMoviesByTitle().values();
        List<Movie> movies = new ArrayList<>(coll);
        Collections.sort(movies, new TitleComparator());
        for(Movie movie: movies) {
            System.out.println(movie.getTitle());
        }
    }
}



























