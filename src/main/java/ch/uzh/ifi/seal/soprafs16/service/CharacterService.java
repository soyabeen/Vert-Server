package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.Ability;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by soyabeen on 24.03.16.
 */
@Service("characterService")
public class CharacterService {

    public Collection<Ability> listCharactersForGame(long gameId) {
        Collection<Ability> result = new ArrayList<>();
        result.add(Ability.BELLE);
        result.add(Ability.DJANGO);
        return result;
    }
}
