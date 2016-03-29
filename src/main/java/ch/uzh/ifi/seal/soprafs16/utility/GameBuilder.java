package ch.uzh.ifi.seal.soprafs16.utility;

import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.User;

import java.util.*;

/**
 * Created by alexanderhofmann on 29/03/16.
 */
public class GameBuilder {

    private Game game;

    private List<Character> availableCharacter;

    public GameBuilder(String name, String owner) {
        game = new Game();
        game.setName(name);
        game.setOwner(owner);
        availableCharacter = new ArrayList<>(Arrays.asList(Character.values()));
    }

    /**
     * Adds a user to game.
     *
     * @param user
     * @return
     */
    public GameBuilder addUser(User user) {
        game.addUser(user);
        return this;
    }

    /**
     * Adds a random user to game.
     *
     * @return
     */
    public GameBuilder addRandomUser() {
        User user = UserBuilder.getRandomUser();
        addUser(user);
        return this;
    }

    /**
     * Adds a user with a random player to a game.
     *
     * @return
     */
    public GameBuilder addRandomUserAndRandomPlayer() {
        List<Character> availableChars = availableCharacter;

        if (!availableChars.isEmpty()) {
            // get player with random character
            Collections.shuffle(availableChars);
            Character character = availableChars.remove(0);
            UserBuilder.getRandomUserWithPlayer(character);

            addRandomUserAndPlayer(character);
        }
        return this;
    }

    /**
     * Adds a user with a player to a game.
     *
     * @param character
     * @return
     */
    public GameBuilder addRandomUserAndPlayer(Character character) {
        List<Character> availableChars = availableCharacter;

        if (!availableChars.isEmpty() && availableChars.contains(character)) {
            User user = UserBuilder.getRandomUserWithPlayer(character);

            game.addUser(user);
        }

        return this;
    }

    public Game build() {
        return game;
    }
}
