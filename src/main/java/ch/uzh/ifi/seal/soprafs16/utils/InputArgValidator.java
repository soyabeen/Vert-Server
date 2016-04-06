package ch.uzh.ifi.seal.soprafs16.utils;

import ch.uzh.ifi.seal.soprafs16.exception.InvalidInputException;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.UserRepository;
import org.springframework.data.repository.CrudRepository;


/**
 * Created by soyabeen on 31.03.16.
 */
public class InputArgValidator {

    private static final String MESSAGE_START = "Invalid arg : ";

    /**
     * Check in the given <code>java.lang.Object</code> is not null, otherwise throw exception.
     *
     * @param arg     The object to check.
     * @param argName Name of the argument shown in the error message.
     * @throws InvalidInputException to indicate the checks negative result.
     */
    public static void checkNotNull(Object arg, String argName) {
        if (arg == null) {
            throw new InvalidInputException(MESSAGE_START + argName + " can not be empty.");
        }
    }

    /**
     * Check in the given <code>java.lang.String</code> is not null or lengt > 0, otherwise throw exception.
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

    public static void checkIfPositiveNumber(Long arg, String argName) {
        if (arg == null || arg.longValue() < 0) {
            throw new InvalidInputException(MESSAGE_START + argName + " no positive number.");
        }
    }

    /**
     * Check in the given user repository for occurrances of the given user name. The check passes if no occurrance
     * is found and fails otherwise.
     *
     * @param playerName The username to check.
     * @param repo     The Spring CRUDRepository for users.
     * @param argName  Name of the argument shown in the error message.
     * @throws InvalidInputException to indicate the checks negative result.
     */
    public static void checkUserNameNotUsed(String playerName, PlayerRepository repo, String argName) {
        if (repo.findByUsername(playerName) != null) {
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
    public static Player checkTokenHasValidPlayer(String token, PlayerRepository repo, String argName) {
        InputArgValidator.checkNotEmpty(token, "token");

        Player holder = repo.findByToken(token);
        if (holder == null) {
            throw new InvalidInputException(MESSAGE_START + " No valid user for " + argName + ".");
        }
        return holder;
    }

    /**
     * Check in the given crud repository for an existing id. If a object object belonging to the id can be found,
     * return the object. Otherwise throw exception.
     *
     * @param id      The id to check.
     * @param repo    The Spring CRUDRepository for the id.
     * @param argName Name of the argument shown in the error message.
     * @return The user belonging to the token,
     * @throws InvalidInputException to indicate the checks negative result.
     */
    public static Object checkAvailabeId(Long id, CrudRepository repo, String argName) {
        InputArgValidator.checkNotNull(id, argName);
        Object e = repo.findOne(id);
        if (e == null) {
            throw new InvalidInputException(MESSAGE_START + " No valid object for " + argName + " " + id + ".");
        }
        return e;
    }
}
