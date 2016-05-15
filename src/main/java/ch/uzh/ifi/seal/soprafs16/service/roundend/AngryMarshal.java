package ch.uzh.ifi.seal.soprafs16.service.roundend;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devuser on 15.05.2016.
 */
public class AngryMarshal implements RoundEnd{

    @Override
    public List<Positionable> execute(Game game, List<Positionable> players) {
        List<Positionable> result = new ArrayList<>();

        // all players on the roof of the waggon where the marshal is in get a bullet card
        for(Positionable player : players) {
            if(player.getLevel() == Positionable.Level.TOP
                    && player.getCar() == game.getPositionMarshal()) {
                ((Player) player).getsShot();
                result.add(player);
            }
        }
        return result;
    }
}
