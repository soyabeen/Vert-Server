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

    public Player getRandomPlayer() {
        Player player = new Player();

        List<Character> availableChars = new ArrayList<>(Arrays.asList(Character.values()));
        Collections.shuffle(availableChars);
        Character character = availableChars.remove(0);

        player.setCharacter(character);

        return playerRepo.save(player);
    }

    public Player getPlayer(Character character) {
        Player player = new Player();
        player.setCharacter(character);

        return playerRepo.save(player);
    }
}
