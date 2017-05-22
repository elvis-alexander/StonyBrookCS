
import java.util.Scanner;

public class DownloadManager {
    // static scanner class
    private static Scanner s = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Hello and welcome to the Download Scheduler.\n");
        System.out.print("Please enter a number of servers: ");
        int numServers = Integer.parseInt(s.next());
        System.out.print("Please enter a download speed: ");
        int downloadSpeed = Integer.parseInt(s.next());
        System.out.print("Please enter a length of time: ");
        int timeLength = Integer.parseInt(s.next());
        System.out.print("Please enter a probability of new premium job per timestep: ");
        double premiumProb = Double.parseDouble(s.next());
        System.out.print("Please enter a probability of new regular job per timestep: ");
        double regularProb = Double.parseDouble(s.next());
        DownloadScheduler scheduler = new DownloadScheduler(timeLength, numServers, downloadSpeed, premiumProb, regularProb);
        System.out.println(scheduler.simulate());
        System.out.println("----------------------Thank You For Running the Simulator--------------");
    }

}
