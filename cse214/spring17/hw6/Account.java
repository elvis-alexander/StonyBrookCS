import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class will contain the information about a User that is protected by the User’s password.
 */
public class Account implements Serializable {
    // The list of followers the User described by this object has.
    private List<User> followers;
    // The list of Users the User described by this object is currently following.
    private List<User> following;
    // This is the user's actual name
    private String name;
    // The password corresponding to the User account.
    private Password password;

    /**
     * This class initializes an account with a users name
     * and a password field as well as list of followers and following
     * @param name
     * @param password
     */
    public Account(String name, Password password) {
        this.name = name;
        this.password = password;
        this.followers = new ArrayList<>();
        this.following = new ArrayList<>();
    }

    /**
     * Returns followers
     * @return
     */
    public List<User> getFollowers() {
        return followers;
    }

    /**
     * Sets followers
     * @param followers
     */
    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    /**
     * Gets followers
     * @return
     */
    public List<User> getFollowing() {
        return following;
    }

    /**
     * Sets followers
     * @param following
     */
    public void setFollowing(List<User> following) {
        this.following = following;
    }

    /**
     * Gets user name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Sets user name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets user pw
     * @return
     */
    public Password getPassword() {
        return password;
    }

    /**
     * Sets user pw
     * @param password
     */
    public void setPassword(Password password) {
        this.password = password;
    }

    /**
     * Adds a user to followers list
     * @param user
     */
    public void addFollower(User user) {
        this.followers.add(user);
    }

    /**
     * Ads a user to following list
     * @param user
     */
    public void addFollowing(User user) {
        this.following.add(user);
    }
}
