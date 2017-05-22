import java.io.Serializable;

/**
 * This class represents a user
 * with a name attribute
 */
public class User implements Serializable {
    // user name
    private String name;

    /**
     * No-arg constructor
     */
    public User() {
    }

    /**
     * Name constructor
     * @param name
     */
    public User(String name) {
        this.name = name;
    }

    /**
     * Retrieves name from user
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Sets a name for a user
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Print format for User class
     * @return
     */
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                '}';
    }
}
