package ch.uzh.ifi.seal.soprafs16.service.roundend;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.List;

/**
 * Created by devuser on 15.05.2016.
 */
public class Hostage implements RoundEnd {

    @Override
    public List<Positionable> execute(Game game, List<Positionable> players) {
        // all players get 250$ reward
        return null;
    }
}
