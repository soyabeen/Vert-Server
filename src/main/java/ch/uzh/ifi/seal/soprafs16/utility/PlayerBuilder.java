package ch.uzh.ifi.seal.soprafs16.utility;

import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by alexanderhofmann on 30/03/16.
 */
@Service
public class PlayerBuilder {

    @Autowired
    private PlayerRepository playerRepo;

    /**
     * Creates a random player and saves it.
     * @return random player
     */
    public Player getRandomPlayer() {
        Player player = new Player();

        List<Character> availableChars = new ArrayList<>(Arrays.asList(Character.values()));
        Collections.shuffle(availableChars);
        Character character = availableChars.remove(0);

        player.setCharacter(character);

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

}
