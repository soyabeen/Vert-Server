package ch.uzh.ifi.seal.soprafs16.utility;

import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Loot;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.LootRepository;
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
    private UserBuilder userBuilder;

    @Autowired
    private LootBuilder lootBuilder;

    @Autowired
    private LootRepository lootRepo;

    @Autowired
    private GameRepository gameRepo;


    public GameBuilder builder(String name, String owner) {
        game = new Game();
        game.setName(name);
        game.setOwner(owner);
        availableCharacter = new ArrayList<>(Arrays.asList(Character.values()));

        return save();
    }

    /**
     * Adds a user to game.
     * @param user
     * @return
     */
    public GameBuilder addUser(User user) {
        game.addUser(user);
        return save();
    }

    /**
     * Adds a random user to game.
     * @return
     */
    public GameBuilder addRandomUser() {
        User user = userBuilder.getRandomUser();
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
            User user = userBuilder.getRandomUserWithPlayer(character);

            game.addUser(user);
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
        return this;
    }

    /**
     * Adds a piece of loot to a game and saves it.
     * @param loot
     * @return
     */
    public GameBuilder addLootAndSave(Loot loot) {
        game.addLoot(loot);
        save();
        return this;
    }

    /**
     * Adds a random piece of loot.
     * @return
     */
    public GameBuilder addRandomLoot() {
        return addLootAndSave(lootBuilder.getRandomLootAndSave());
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
