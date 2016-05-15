package ch.uzh.ifi.seal.soprafs16.service.roundend;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.List;

/**
 * Created by devuser on 15.05.2016.
 */
public class MarshalsRevenge implements RoundEnd {

    @Override
    public List<Positionable> execute(Game game, List<Positionable> players) {
        // all players on the roof of the waggon where the marshal is in lose purse loot
        // if no purse loot on player he looses nothing
        return null;
    }
}
