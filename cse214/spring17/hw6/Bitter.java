import java.io.Serializable;

public class Bitter implements Serializable {
    private UserDatabase users;
    private AccountDatabase accounts;

    /**
     * no-arg constructor
     */
    public Bitter() {
        this.users = new UserDatabase();
        this.accounts = new AccountDatabase();
    }

    /**
     * Manually inserts a User into the UserDatabase and
     * Account into the AccountDatabase both with the specified key (email).
     * @param email
     * @param user
     * @param account
     */
    public void addUser(String email, User user, Account account) {
        if(account == null || user == null || users.containsKey(email) || accounts.containsKey(email))
            throw new IllegalArgumentException("Verify email is not null, or may be used already.");
        users.addUser(email, user);
        accounts.addAccount(email, account);
    }

    /**
     * This method removes a User from the social network.
     * @param email
     * @throws IllegalArgumentException
     */
    public void removeUser(String email) throws IllegalArgumentException {
        if(email == null || !users.containsKey(email) || !accounts.containsKey(email))
            throw new IllegalArgumentException("User email may be null or the given email is not in the social network.");
        users.removeUser(email);
        accounts.removeAccount(email);
    }

    /**
     * Returns users database
     * @return
     */
    public UserDatabase getUsers() {
        return users;
    }

    /**
     * Sets users database
     * @param users
     */
    public void setUsers(UserDatabase users) {
        this.users = users;
    }

    /**
     * Gets account database
     * @return
     */
    public AccountDatabase getAccounts() {
        return accounts;
    }

    /**
     * Sets account database
     * @param accounts
     */
    public void setAccounts(AccountDatabase accounts) {
        this.accounts = accounts;
    }
}
