package ch.uzh.ifi.seal.soprafs16.service.roundend;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.List;

/**
 * Created by devuser on 15.05.2016.
 */
public class Pickpocketing implements RoundEnd {

    @Override
    public List<Positionable> execute(Game game, List<Positionable> players) {
        // all players which are alone can pick up loot (if there is any)
        return null;
    }
}
