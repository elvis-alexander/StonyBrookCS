import java.io.Serializable;

/**
 * This class represents a wrapper for a valid password
 */
public class Password implements Serializable {
    // password specified by user
    private String password;

    /**
     * Constructs a new Password from a given pw
     * Verifies password includes 1-upper,1-lower,1-special,1-number
     * @param initPassword
     * @throws IllegalArgumentException
     */
    public Password(String initPassword) throws IllegalArgumentException {
        boolean hasUpperCase = ! initPassword.equals(initPassword.toLowerCase());
        boolean hasLowerCase = ! initPassword.equals(initPassword.toUpperCase());
        boolean hasSpecial = ! initPassword.matches("[A-Za-z0-9 ]*");
        boolean hasNumber = initPassword.matches(".*\\d+.*");
        // throw exception on invalid pw
        if (! (hasUpperCase && hasLowerCase && hasSpecial && hasNumber))
            throw new IllegalArgumentException(
                    "Your password is not secure enough. Please make sure you have at " +
                    "least one upper case and one lower case letter, one special character, and one number."
            );
        // set pw
        this.password = initPassword;
    }

    /**
     * Returns password field
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password field
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
