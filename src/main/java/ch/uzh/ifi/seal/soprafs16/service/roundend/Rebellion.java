package ch.uzh.ifi.seal.soprafs16.service.roundend;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Marshal;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import ch.uzh.ifi.seal.soprafs16.utils.TargetFinder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devuser on 15.05.2016.
 */
public class Rebellion implements RoundEnd {

    @Override
    public List<Positionable> execute(Game game) {
        // all players inside the train get a bullet card
        List<Positionable> result = new ArrayList<>();

        TargetFinder targetFinder = new TargetFinder();
        List<Player> playersInsideTrain = new ArrayList<>();
        // all players inside of train
        for (int i = 0; i < game.getNrOfCars(); ++i) {
            playersInsideTrain.addAll(targetFinder.filterPlayersOnSameFloor(new Marshal(i), game.getPlayers()));
        }

        for(Player target : playersInsideTrain) {
            target.getsShot();
            result.add(target);
        }

        return result;
    }
}
