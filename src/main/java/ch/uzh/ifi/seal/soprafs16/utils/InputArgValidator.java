package ch.uzh.ifi.seal.soprafs16.utils;

import ch.uzh.ifi.seal.soprafs16.exception.InvalidInputException;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.repositories.UserRepository;

/**
 * Created by soyabeen on 31.03.16.
 */
public class InputArgValidator {

    private static final String MESSAGE_START = "Invalid arg : ";

    /**
     * Check in the given <code>java.lang.String</code> is null or lengt <= 0.
     *
     * @param arg     The String argument to check.
     * @param argName Name of the argument shown in the error message.
     * @throws InvalidInputException to indicate the checks negative result.
     */
    public static void checkNotEmpty(String arg, String argName) {
        if (arg == null || arg.length() <= 0) {
            throw new InvalidInputException(MESSAGE_START + argName + " can not be empty.");
        }
    }

    /**
     * Check in the given user repository for occurrances of the given user name. The check passes if no occurrance
     * is found and fails otherwise.
     *
     * @param userName The username to check.
     * @param repo     The Spring CRUDRepository for users.
     * @param argName  Name of the argument shown in the error message.
     * @throws InvalidInputException to indicate the checks negative result.
     */
    public static void checkUserNameNotUsed(String userName, UserRepository repo, String argName) {
        if (repo.findByUsername(userName) != null) {
            throw new InvalidInputException(MESSAGE_START + argName + " is already in use.");
        }
    }

    /**
     * Check in the given user repository for a user belonging to the given token. If a user object can be found,
     * return it. Otherwise throw exception.
     *
     * @param token   The token to check.
     * @param repo    The Spring CRUDRepository for users.
     * @param argName Name of the argument shown in the error message.
     * @return The user belonging to the token,
     * @throws InvalidInputException to indicate the checks negative result.
     */
    public static User checkTokenHasValidUser(String token, UserRepository repo, String argName) {
        InputArgValidator.checkNotEmpty(token, "token");

        User holder = repo.findByToken(token);
        if (holder == null) {
            throw new InvalidInputException(MESSAGE_START + " No valid user for " + argName + ".");
        }
        return holder;
    }
}
