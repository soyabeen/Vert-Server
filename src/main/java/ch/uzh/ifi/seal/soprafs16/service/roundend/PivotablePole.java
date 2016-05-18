package ch.uzh.ifi.seal.soprafs16.service.roundend;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import ch.uzh.ifi.seal.soprafs16.utils.TargetFinder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devuser on 15.05.2016.
 */
public class PivotablePole implements RoundEnd {

    private TargetFinder targetFinder;

    public PivotablePole() {
        targetFinder = new TargetFinder();
    }

    /**
     * All players on the top, will be moved to the last car.
     *
     * @param game
     * @return list of players moved.
     */
    @Override
    public List<Positionable> execute(Game game) {
        List<Positionable> result = new ArrayList<>();
        List<Player> playersOnRoof = targetFinder.filterPlayersByLevel(game.getPlayers(), Positionable.Level.TOP);
        for (Player player : playersOnRoof) {
            player.setCar(game.getNrOfCars() - 1);
            result.add(player);
        }
        return result;
    }
}
