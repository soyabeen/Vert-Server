package ch.uzh.ifi.seal.soprafs16.service;


import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by soyabeen on 24.03.16.
 */
@Service("characterService")
public class CharacterService {

    @Autowired
    private PlayerService playerService;

    public List<Character> listAvailableCharactersByGame(long gameId) {
        List<Character> result = listCharacters();
        for (Player player : playerService.listPlayersForGame(gameId)) {
            result.remove(player.getCharacter());
        }
        return result;
    }

    public List<Character> listCharacters() {
        return new ArrayList<>(Arrays.asList(Character.values()));
    }
}
