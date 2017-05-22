import java.io.*;
import java.util.Map;
import java.util.Scanner;


public class BitterPlatform {
    // this will hold the current state of the site
    private static Bitter bitter = null;
    // represents current logged inL user
    private static Account currentAccount = null;
    private static String currentEmail = null;
    // controls whether or not program is running
    private static boolean run = true;
    // scanner for class
    private static Scanner systemInput = new Scanner(System.in);
    // serialized file
    private static String fileName = "Bitter.ser";
    private static File serializedFile = new File(fileName);
    // static helper messages
    private static String welcomeMsg = "Hello, and welcome to Bitter, a more tasteful social network. No previous data found.";
    private static String alternativeWelcomeMsg = "Hello, and welcome to Bitter, a more tasteful social network. Bitter.ser loaded.";
    private static String initMsg = "You are not logged in.\n\tL) Login\n\tS) Sign Up\n\tQ) Quit.";
    private static String optionMsg = "Please select an option: ";
    private static String initErrMsg = "Please specify a valid option (L, S, Q)";
    private static String menuErrMsg = "Please specify a valid option (F, U, V, S, A, L, C)";

    /**
     * Main execution of program
     * @param args
     */
    public static void main(String[] args) {
        while (run) {
            try {
                if (bitter == null) {
                    // load bitter if possible, create new otherwise
                    if (serializedFile.exists()) {
                        loadSession();
                        System.out.println(alternativeWelcomeMsg);
                    } else {
                        newSession();
                        bitter = new Bitter();
                        System.out.println(welcomeMsg);
                    }
                }
                if (!isLoggedIn()) {
                    // present login options
                    System.out.println("You are not logged in.\n");
                    loginMenu();
                    continue;
                }
                // user is logged in from this point
                char mainMenuOption = printMenu();
                switch (mainMenuOption) {
                    case 'F':
                        followUser();
                        break;
                    case 'U':
                        unfollowUser();
                        break;
                    case 'V':
                        viewFollowers();
                        break;
                    case 'S':
                        viewFollowings();
                        break;
                    case 'A':
                        listAllUsers();
                        break;
                    case 'L':
                        logout();
                        break;
                    case 'C':
                        closeAccount();
                        break;
                }
            } catch (Exception exception) {
                System.out.println(String.format(
                        "Uhoh- looks like something went wrong, here is the error message: %s", exception.getMessage())
                );
            }
        }
    }

    /**
     * Creates a new session in the case of no saved file
     */
    private static void newSession() {
        bitter = new Bitter();
    }

    /**
     * Loads a current session at initialization time
     * @throws Exception
     */
    private static void loadSession() throws Exception {
        FileInputStream file = new FileInputStream(fileName);
        ObjectInputStream inStream = new ObjectInputStream(file);
        bitter = (Bitter) inStream.readObject();
    }

    /**
     * Serializes bitter to file
     * @throws IOException
     */
    private static void saveState() throws Exception {
        FileOutputStream file = new FileOutputStream(fileName);
        ObjectOutputStream outStream = new ObjectOutputStream(file);
        outStream.writeObject(bitter);
        outStream.close();
    }

    /**
     * Allows user to select to login, signup, or quit
     */
    private static void loginMenu() throws Exception {
        System.out.println(initMsg);
        System.out.print(optionMsg);
        String s = systemInput.nextLine();
        if (s == null || s.length() < 1)
            throw new IllegalArgumentException(initErrMsg);
        char setupOption = s.toUpperCase().charAt(0);
        switch (setupOption) {
            case 'L':
                login();
                break;
            case 'S':
                signUserUp();
                break;
            case 'Q':
                quit();
                break;
            default:
                throw new IllegalArgumentException(initErrMsg);
        }
    }

    /**
     * Creates a new user Bitter and saves it to database
     */
    private static void signUserUp() {
        System.out.print("Please enter your email: ");
        String email = systemInput.nextLine();
        System.out.print("Please enter your name: ");
        String name = systemInput.nextLine();
        Password password = null;
        while (true) {
            System.out.print("Please enter a password: ");
            try {
                String pw = systemInput.nextLine();
                password = new Password(pw);
                break;
            } catch (IllegalArgumentException exception) {
                System.out.println(exception.getMessage());
            }
        }
        User user = new User(name);
        Account account = new Account(name, password);
        bitter.addUser(email, user, account);
        currentAccount = account;
        currentEmail = email;
        System.out.print("Thanks for signing up! Welcome to Bitter!\n");
    }

    /**
     * Logs user in
     */
    private static void login() {
        System.out.print("Please enter your email: ");
        String email = systemInput.nextLine();
        Account account = bitter.getAccounts().getAccountInformation(email);
        if(account == null)
            throw new IllegalArgumentException("User not found.\n");
        System.out.print("Please enter your password: ");
        String pw = systemInput.nextLine();
        if(account.getPassword().getPassword().equals(pw)) {
            currentAccount = account;
            currentEmail = email;
        } else {
            throw new IllegalArgumentException("Invalid credentials, please verify you password");
        }
    }

    /**
     * Terminates program
     * @throws Exception
     */
    private static void quit() throws Exception {
        System.out.println("Shutting down… Current program state saved to Bitter.ser");
        saveState();
        run = false;
    }

    /**
     * Prints the main menu and return user input
     */
    private static char printMenu() throws IllegalArgumentException {
        System.out.println(
                "Menu:\n\tF) Follow someone\n\tU) Unfollow someone" +
                "\n\tV) View Followers\n\tS) See who you follow." +
                "\n\tA) List all users.\n\tL) Logout.\n\tC) Close your account.\n"
        );
        System.out.print(optionMsg);
        String s = systemInput.nextLine();
        if (s == null || s.length() < 1)
            throw new IllegalArgumentException(menuErrMsg);
        return s.toUpperCase().charAt(0);
    }


    /**
     * returns true if user is logged on, false otherwise
     */
    private static boolean isLoggedIn() {
        return currentAccount != null;
    }

    /**
     * Follows a user
     * @throws IllegalArgumentException
     */
    private static void followUser() throws IllegalArgumentException {
        System.out.print("Please enter the email of the person you would like to follow: ");
        // add to list of followers
        String email = systemInput.nextLine();
        User followingUser = bitter.getUsers().getUser(email);
        if(followingUser == null)
            throw new IllegalArgumentException("User does not exists email: " + email);
        currentAccount.addFollowing(followingUser);
        // add current user to following user followees account
        Account followeeAccount = bitter.getAccounts().getAccountInformation(email);
        User currentUser = bitter.getUsers().getUser(currentEmail);
        followeeAccount.addFollower(currentUser);
        System.out.println(String.format("%s has been successfully followed.", followingUser.getName()));
    }

    private static void unfollowUser() throws Exception {
        System.out.print("Please enter the email of the person you would like to unfollow: ");
        // remove from current user following list
        String email = systemInput.nextLine();
        User user = bitter.getUsers().getUser(email);
        currentAccount.getFollowing().remove(user);
        // remove current user from follower followings list
        Account followerAccount = bitter.getAccounts().get(email);
        User currentUser = bitter.getUsers().getUser(currentEmail);
        followerAccount.getFollowers().remove(currentUser);
        System.out.println(user.getName() + " unfollowed");

    }

    /**
     *
     */
    private static void viewFollowers() {
        System.out.println(
                String.format("Your followers:\n%-40s%-40s\n%s",
                        "Email",
                        "Name",
                        "---------------------------------------------------------------------------------------------------"
                )
        );
        for(User followingUser: currentAccount.getFollowers()) {
            for(Map.Entry<String, User> usersDb: bitter.getUsers().entrySet()) {
                if(followingUser == usersDb.getValue()) {
                    System.out.println(String.format("%-40s%-40s", usersDb.getKey(), usersDb.getValue().getName()));
                    break;
                }
            }
        }
    }

    private static void viewFollowings() {
        System.out.println(
                String.format("You follow:\n%-40s%-40s\n%s",
                        "Email",
                        "Name",
                        "---------------------------------------------------------------------------------------------------"
                )
        );
        for(User followingUser: currentAccount.getFollowing()) {
            for(Map.Entry<String, User> usersDb: bitter.getUsers().entrySet()) {
                if(followingUser == usersDb.getValue()) {
                    System.out.println(String.format("%-40s%-40s", usersDb.getKey(), usersDb.getValue().getName()));
                    break;
                }
            }
        }
    }

    private static void listAllUsers() {
        System.out.println(
                String.format("All Users:\n%-40s%-40s\n%s",
                        "Email",
                        "Name",
                        "---------------------------------------------------------------------------------------------------"
                )
        );
        for(Map.Entry<String, User> entry: bitter.getUsers().entrySet()) {
            System.out.println(String.format("%-40s%-40s", entry.getKey(), entry.getValue().getName()));
        }
    }

    /**
     * Logs user out
     */
    private static void logout() {
        currentAccount = null;
        currentEmail = null;
    }

    /**
     * deletes from account
     */
    private static void closeAccount() {
        // removing followers from
        User currentUser = bitter.getUsers().getUser(currentEmail);
        for(Map.Entry<String, Account> accounts: bitter.getAccounts().entrySet()) {
            Account a = accounts.getValue();
            if(a.getFollowing().contains(currentUser))
                a.getFollowing().remove(currentUser);
            if(a.getFollowers().contains(currentUser))
                a.getFollowers().remove(currentUser);
        }
        System.out.println(String.format("%s account has been closed.", currentAccount.getName()));
        bitter.removeUser(currentEmail);
        currentAccount = null;
        currentEmail = null;
    }
}
