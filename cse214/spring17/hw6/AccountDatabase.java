import java.io.Serializable;
import java.util.HashMap;

/**
 *  Email of the User objects is the key for Account
 */
public class AccountDatabase extends HashMap<String, Account> implements Serializable {
    /**
     * This method adds a new Account into the AccountDatabase using the specified User email as the key.
     * @param email
     * @param account
     * @throws IllegalArgumentException
     */
    public void addAccount(String email, Account account) throws IllegalArgumentException {
        if(email == null || this.containsKey(email))
            throw new IllegalArgumentException("Must contain a unique email");
        this.put(email, account);
    }

    /**
     * Retrieves the Account from the table having the indicated email.
     * If the requested email does not exist in the AccountDatabase, return null.
     * @param email
     * @return
     */
    public Account getAccountInformation(String email) {
        if(!this.containsKey(email))
            return null;
        return this.get(email);
    }

    /**
     * This method removes an Account from the AccountDatabase.
     * @param email
     */
    public void removeAccount(String email) throws IllegalArgumentException {
        if(email == null || !this.containsKey(email))
            throw new IllegalArgumentException("Account does not exist");
        this.remove(email);
    }
}
