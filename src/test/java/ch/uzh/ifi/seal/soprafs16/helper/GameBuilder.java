package ch.uzh.ifi.seal.soprafs16.helper;

import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Loot;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by alexanderhofmann on 29/03/16.
 */
@Service
public class GameBuilder {

    private Game game;

    private List<Character> availableCharacter;

    @Autowired
    private PlayerBuilder playerBuilder;

    @Autowired
    private LootBuilder lootBuilder;

    @Autowired
    private GameRepository gameRepo;

    /**
     * Creates a game and saves it.
     * @param name
     * @param owner
     * @return
     */
    public GameBuilder init(String name, String owner) {
        game = new Game();
        game.setName(name);
        game.setOwner(owner);
        game.setStatus(GameStatus.PENDING);
        availableCharacter = new ArrayList<>(Arrays.asList(Character.values()));

        return save();
    }

    /**
     * Creates a game without saving it.
     * @param name
     * @param owner
     * @return
     */
    public GameBuilder initNoPersistence(String name, String owner) {
        game = new Game();
        game.setName(name);
        game.setOwner(owner);
        availableCharacter = new ArrayList<>(Arrays.asList(Character.values()));

        return this;
    }

    /**
     * Adds a user to game.
     * @param user
     * @return
     */
    public GameBuilder addUser(Player user) {
        game.addPlayer(user);
        return save();
    }

    /**
     * Adds multiple users.
     * @param users
     * @return
     */
    public GameBuilder addUsers(Player... users) {
        for (Player user : users) {
            addUser(user);
        }
        return this;
    }

    /**
     * Adds a random user to game.
     * @return
     */
    public GameBuilder addRandomUser() {
        Player user = playerBuilder.init(true).build();
        return addUser(user);
    }

    /**
     * Adds a user with a random player to a game.
     * @return
     */
    public GameBuilder addRandomUserAndRandomPlayer() {
        List<Character> availableChars = availableCharacter;

        if (!availableChars.isEmpty()) {
            // get player with random character
            Collections.shuffle(availableChars);
            Character character = availableChars.remove(0);
            //userBuilder.getRandomUserWithPlayer(character);

            addRandomUserAndPlayer(character);
        }
        return save();
    }

    /**
     * Adds a user with a player to a game.
     * @param character
     * @return
     */
    public GameBuilder addRandomUserAndPlayer(Character character) {
        List<Character> availableChars = availableCharacter;

        if (!availableChars.isEmpty() && availableChars.contains(character)) {
            Player user = playerBuilder.init(true).addCharacter(character).build();

            game.addPlayer(user);
        }

        return save();
    }

    /**
     * Adds a piece of loot to a game.
     * @param loot
     * @return
     */
    public GameBuilder addLoot(Loot loot) {
        game.addLoot(loot);
        return save();
    }

    /**
     * Adds a piece of loot to a game and saves it.
     * @param loot
     * @return
     */
    public GameBuilder addLootAndSave(Loot loot) {
        game.addLoot(loot);
        return save();
    }

    /**
     * Adds a random piece of loot.
     * @return
     */
    public GameBuilder addRandomLoot() {
        return addLoot(lootBuilder.init(game.getId()).build());
    }

    public GameBuilder setStatus(GameStatus gameStatus) {
        game.setStatus(gameStatus);
        return save();
    }

    /**
     * Builds the configured game.
     * @return
     */
    public Game build() {
        return game;
    }

    /**
     * Saves the current configuration to repository
     * @return
     */
    private GameBuilder save() {
        game = gameRepo.save(game);
        return this;
    }
}
