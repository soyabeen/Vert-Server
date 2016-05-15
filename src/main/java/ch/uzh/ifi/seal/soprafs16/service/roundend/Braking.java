package ch.uzh.ifi.seal.soprafs16.service.roundend;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devuser on 15.05.2016.
 */
public class Braking implements RoundEnd {

    @Override
    public List<Positionable> execute(Game game, List<Positionable> players) {
        List<Positionable> result = new ArrayList<>();

        // all players on the roof will be positioned 1 waggon ahead (TO_HEAD)
        for(Positionable player : players) {
            if(player.getLevel() == Positionable.Level.TOP) {
                int currentCar = player.getCar();
                player.setCar( currentCar <= 0 ? 0: currentCar-1 );
                result.add(player);
            }
        }
        return result;
    }

}
