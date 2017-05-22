
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import big.data.DataSource;


public class ASMDB {
    public static void main(String[] args) {
        System.out.println("Welcome to A Smiley Movie Data Base, the happiest way to manage your DVDs.\n");
        System.out.println("Menu:\n" +
                "\tI - Import Movie <Title>\n" +
                "\tD - Delete Movie <Title>\n" +
                "\tS - Sort Movies\n" +
                "\t\tBy Title Ascending (TA)\n" +
                "\t\tBy Title Descending (TD)\n" +
                "\t\tBy Release Date Ascending (YA)\n" +
                "\t\tBy Release Date Descending (YD)\n" +
                "\tA - Sort Actors\n" +
                "\t\tAlphabetically Ascending (AA)\n" +
                "\t\tAlphabetically Descending (AD)\n" +
                "\t\tBy Number of Movies They Are In Ascending (NA)\n" +
                "\t\tBy Number of Movies They Are In (ND)\n" +
                "\tQ - Quit");
        MovieManager manager = new MovieManager();

        while (true) {
            System.out.println("Main Menu:\n" +
                    "\tI) Import a Movie\n" +
                    "\tD) Delete a Movie\n" +
                    "\tA) Sort Actors\n" +
                    "\tM) Sort Movies\n" +
                    "\tQ) Quit");
            System.out.print("Please select an option: ");
            String s = new Scanner(System.in).nextLine().toUpperCase();
            if(s.equals("")) continue;
            switch (s.charAt(0)) {
                case 'I':
                    System.out.print("Please enter a movie title: ");
                    String movie_i = new Scanner(System.in).nextLine();
                    String prefix = "http://www.omdbapi.com/?t=";
                    String postfix = "&y=&plot=short&r=xml";
                    DataSource ds = DataSource.connectXML(prefix+movie_i.replace(' ','+')+postfix).load();
                    if(ds.fetchString("response").equalsIgnoreCase("false")) {
                        System.out.println("Movie not found.");
                        break;
                    }
                    // fetch data
                    // new Movie(title, year)
                    String title = ds.fetchString("movie/title");
                    int year =  ds.fetchInt("movie/year");
                    // new Actor(actorName);
                    String[] actors = ds.fetchString("movie/actors").split(", ");
                    // data
                    Movie newM = new Movie(title, year);
                    for(String a : actors) {
                        Actor newA = manager.getSpecifiedActor(a);
                        if(newA == null) {
                            newA = new Actor(a, 1);
                            manager.getActors().add(newA);
                        } else {
                            newA.increaseCount();
                        }
                        newM.getActors().add(newA);
                    }
                    manager.getMovies().add(newM);
                    System.out.println(String.format("Movie added: \"%s\"", title));
                    break;
                case 'D':
                    System.out.print("Please enter the movie title to be deleted: ");
                    String movie_d = new Scanner(System.in).nextLine();
                    /* delete from movie list and delete from  */
                    int movie_index = manager.indexOfMovie(movie_d);
                    if(movie_index == -1) {
                        System.out.println("Movie does not exist in this list");
                        break;
                    }

                    Movie toDelete = manager.getMovies().get(movie_index);
                    for(int i = 0; i < toDelete.getActors().size(); ++i) {
                        toDelete.getActors().get(i).decrementCount();
                        if(toDelete.getActors().get(i).getCount() <= 0) {
                            manager.removeActor(toDelete.getActors().get(i).getName());
                        }
                    }
                    manager.getMovies().remove(movie_index);
                    System.out.println(movie_d + " deleted.");
                    break;
                case 'A':
                    System.out.print("Actor Sorting Options:\n" +
                            "\t\tAA) Alphabetically Ascending\n" +
                            "\t\tAD) Alphabetically Descending\n" +
                            "\t\tNA) By Number of Movies They Are In Ascending\n" +
                            "\t\tND) By Number of Movies They Are In Descending\n" +
                            "Please Select A Sort Method: ");
                    String in = new Scanner(System.in).nextLine().toUpperCase();
                    System.out.println("Actor\t\t\t\t\t\tNumber of Movies\n" +
                            "-------------------------------------------------------------------------------------------");
                    List<Actor> l;
                    switch(in) {
                        case "AA":
                            l = manager.getSortedActors(new NameComparator());
                            for(Actor a : l)
                                System.out.println(a.getName() + "\t\t\t\t" + a.getCount());
                            break;
                        case "AD":
                            l = manager.getSortedActors(new NameComparator());
                            for(int i = l.size() - 1; i >= 0; --i)
                                System.out.println(l.get(i).getName() + "\t\t\t\t" + l.get(i).getCount());
                            break;
                        case "NA":
                            l = manager.getSortedActors(new CountComparator());
                            for(Actor a : l)
                                System.out.println(a.getName() + "\t\t\t\t" + a.getCount());
                            break;
                        case "ND":
                            l = manager.getSortedActors(new CountComparator());
                            for(int i = l.size() - 1; i >= 0; --i)
                                System.out.println(l.get(i).getName() + "\t\t\t\t" + l.get(i).getCount());
                            break;
                        default:
                            System.out.println("not a valid sort");
                            break;
                    }
                    break;
                case 'M':
                    System.out.print("Movie Sorting Options:\n" +
                            "\t\tTA) Title Ascending (A-Z)\n" +
                            "\t\tTD) Title Descending (Z-A)\n" +
                            "\t\tYA) Year Ascending\n" +
                            "\t\tYD) Year Descending\n" +
                            "Please Select A Sort Method: ");
                    String inn = new Scanner(System.in).nextLine().toUpperCase();
                    System.out.println(String.format("Title\t\tYear\t\tActors"));
                    System.out.println("-------------------------------------------------------------------------------------------");
                    List<Movie> lm;
                    switch(inn) {
                        case "TA":
                            lm = manager.getSortedMovies(new TitleComparator());
                            for(Movie m : lm)
                                System.out.println(String.format("%s\t\t%s\t\t%s", m.getTitle(), ""+m.getYear(), m.actorsToString()));
                            break;
                        case "TD":
                            lm = manager.getSortedMovies(new TitleComparator());
                            for(int i = lm.size() - 1; i >= 0; --i)
                                System.out.println(String.format("%s\t\t%s\t\t%s", lm.get(i).getTitle(), ""+lm.get(i).getYear(), lm.get(i).actorsToString()));
                            break;
                        case "YA":
                            lm = manager.getSortedMovies(new YearComparator());
                            for(Movie m : lm)
                                System.out.println(String.format("%s\t\t%s\t\t%s", m.getTitle(), ""+m.getYear(), m.actorsToString()));
                            break;
                        case "YD":
                            lm = manager.getSortedMovies(new YearComparator());
                            for(int i = lm.size() - 1; i >= 0; --i)
                                System.out.println(String.format("%s\t\t%s\t\t%s", lm.get(i).getTitle(), ""+lm.get(i).getYear(), lm.get(i).actorsToString()));
                            break;
                        default:
                            System.out.println("not a valid sort");
                            break;
                    }
                    break;
                case 'Q':
                    System.out.println("That's All Folks!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("please enter a valid option");
                    break;
            }
        }
    }
}
