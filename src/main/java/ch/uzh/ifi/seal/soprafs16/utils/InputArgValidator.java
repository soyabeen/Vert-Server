package ch.uzh.ifi.seal.soprafs16.utils;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs16.exception.InvalidInputException;
import ch.uzh.ifi.seal.soprafs16.model.Card;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by soyabeen on 31.03.16.
 */
public class InputArgValidator {

    private static final String MESSAGE_START = "Invalid arg : ";

    private InputArgValidator() {
        // Non-public constructor since this is a utility class with only static methods.
    }

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

    public static void checkIfIdGreaterZero(Long arg, String argName) {
        if (arg == null || arg.longValue() <= 0) {
            throw new InvalidInputException(MESSAGE_START + argName + " no valid id (must be > 0).");
        }
    }

    public static void checkIfIdGreaterZero(Integer arg, String argName) {
        checkIfIdGreaterZero(arg.longValue(), argName);
    }

    /**
     * Check in the given user repository for occurrances of the given user name. The check passes if no occurrance
     * is found and fails otherwise.
     *
     * @param playerName The username to check.
     * @param repo       The Spring CRUDRepository for users.
     * @param argName    Name of the argument shown in the error message.
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


    /**
     * Check if a request to get possibilities for a turn is made from the player, who's turn it actually is.
     *
     * @param player Made the request for a turn.
     * @param game   Game the request was made in.
     * @throws InvalidInputException to indicate the checks negative result.
     */
    public static void checkItIsPlayersTurn(Player player, Game game) {

        if (!game.getCurrentPlayerId().equals(player.getId())) {
            throw new InvalidInputException(MESSAGE_START + " It is not the player's turn");
        }

    }

    /**
     * Check if player plays card from hand
     *
     * @param type   CardType for card to play
     * @param player Player wants to play card
     * @return found Card on hand
     * @throws InvalidInputException
     */
    public static Card checkIfSuchCardOnHand(CardType type, Player player) {

        if (CardType.DRAW.equals(type)) {
            return new Card(CardType.DRAW, player.getId());
        } else {
            List<Card> cardsOnHand = player.getHand();
            for (Card c : cardsOnHand) {
                if (c.getType().equals(type)) {
                    return c;
                }
            }
        }
        throw new InvalidInputException("Player " + player.getUsername() + " is trying to play the card "
                + type + " which is not in his hand");
    }

    public static Character checkIfCharacterAvailable(Game game, Character character) {
        List<Character> usedCharacter = new ArrayList<>();
        for(Player p: game.getPlayers()) {
            usedCharacter.add(p.getCharacter());
        }
        if(usedCharacter.contains(character))
            throw new InvalidInputException(MESSAGE_START + " Character is already taken");
        return character;
    }

    public static void checkIfOwnerHasCharacter(Player owner) {
        if (owner.getCharacter() != null) return;
        throw  new InvalidInputException(MESSAGE_START + " Owner must choose a character before starting the game");
    }

    public static void checkIfGameIsInPlanningPhase(Game game) {
        if (!game.getStatus().equals(GameStatus.PLANNINGPHASE)) {
            throw new InvalidInputException(MESSAGE_START + "Game is not in Planningphase");
        }
    }

    public static void checkIfPlayerHasCharacter(Player player) {
        if (player.getCharacter() == null) return;
        throw  new InvalidInputException(MESSAGE_START + " Player already has a character");
    }

    public static void checkCorrectRound(Game game, int nthRound) {
        if(game.getRoundId() != nthRound)
            throw new InvalidInputException(MESSAGE_START + "You idiot, check your endpoint ROUNDID");

    }
}
