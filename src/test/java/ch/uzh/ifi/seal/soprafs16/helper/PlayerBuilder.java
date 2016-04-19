package ch.uzh.ifi.seal.soprafs16.helper;

import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by alexanderhofmann on 30/03/16.
 */
@Service
public class PlayerBuilder {

    private Player player;

    @Autowired
    private PlayerRepository playerRepo;

    private boolean isPersistent;

    public PlayerBuilder init() {
        return init(UUID.randomUUID().toString());
    }

    public PlayerBuilder init(boolean isPersistent) {
        this.isPersistent = isPersistent;
        return init();
    }

    public PlayerBuilder init(String username) {
        player = new Player();
        player.setUsername(username);
        player.setToken(UUID.randomUUID().toString());
        return save();
    }

    public PlayerBuilder init(boolean isPersistent, String username) {
        this.isPersistent = isPersistent;
        return init(username);
    }

    public PlayerBuilder addCharacter(Character character) {
        player.setCharacter(character);
        return save();
    }

    public PlayerBuilder addRandomCharacter() {
        return addCharacter(getRandomCharacter());
    }

    private Character getRandomCharacter() {
        List<Character> availableChars = new ArrayList<>(Arrays.asList(Character.values()));
        Collections.shuffle(availableChars);
        return availableChars.remove(0);
    }

    /**
     * Creates a random player and saves it.
     * @return random player
     */
    public Player getRandomPlayer() {
        Player player = new Player();
        player.setUsername(UUID.randomUUID().toString());
        player.setToken(UUID.randomUUID().toString());

        return playerRepo.save(player);
    }

    /**
     * Creates a player with a given character.
     * @param character
     * @return player
     */
    public Player getPlayer(Character character) {
        Player player = new Player();
        player.setCharacter(character);

        return playerRepo.save(player);
    }

    public Player getPlayerNoPersistence(Character character) {
        Player player = new Player();
        player.setCharacter(character);
        return player;
    }

    public List<Player> getPlayers(Character... characters) {
        List<Player> result = new ArrayList<>();

        for (Character c : characters) {
            result.add(getPlayer(c));
        }

        return result;
    }

    public List<Player> getPlayersNoPersistence(Character... characters) {
        List<Player> result = new ArrayList<>();

        for (Character c : characters) {
            result.add(getPlayerNoPersistence(c));
        }

        return result;
    }

    public PlayerBuilder save() {
        if (isPersistent) {
            player = playerRepo.save(player);
        }
        return this;
    }

    public Player build() {
        return player;
    }

}
