import java.io.Serializable;
import java.util.HashMap;

/**
 * This class represents a database
 * which maps emails to users
 */
public class UserDatabase extends HashMap<String, User> implements Serializable {
    /**
     * No-arg constructor
     */
    public UserDatabase() {
    }

    /**
     * Adds a user to the user database
     *
     * @param email
     * @param user
     */
    public void addUser(String email, User user) throws IllegalArgumentException {
        if (email == null || this.containsKey(email))
            throw new IllegalArgumentException("Email is either null or already in use");
        this.put(email, user);
    }

    /**
     * Retrieves a user from the database given name
     *
     * @param email
     * @return
     */
    public User getUser(String email) {
        // return null if user is not in databse
        if (! this.containsKey(email))
            return null;
        // retrieve user by key (email)
        return this.get(email);
    }

    /**
     * Removes a user from the database (if exists)
     * specified by email field
     *
     * @param email
     */
    public void removeUser(String email) {
        if (email == null || !this.containsKey(email))
            throw new IllegalArgumentException("Email is either null or already in user");
        this.remove(email);
    }
}