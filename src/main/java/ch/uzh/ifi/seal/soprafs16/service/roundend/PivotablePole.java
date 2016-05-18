package ch.uzh.ifi.seal.soprafs16.service.roundend;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Marshal;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import ch.uzh.ifi.seal.soprafs16.utils.TargetFinder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devuser on 15.05.2016.
 */
public class PivotablePole implements RoundEnd {

    @Override
    public List<Positionable> execute(Game game) {
        // all players on the roof will be positioned on last waggon
        List<Positionable> result = new ArrayList<>();

        TargetFinder targetFinder = new TargetFinder();
        Marshal marshal = new Marshal(game.getPositionMarshal());
        List<Positionable> affectedPlayers = new ArrayList<>();
        // all players on oppsite floor of marshal (=roof)
        affectedPlayers.addAll( targetFinder.filterPlayersOnOppositeFloor(marshal, game.getPlayers()) );

        for(Positionable player : affectedPlayers) {
            player.setCar(game.getNrOfCars());
            result.add(player);
        }

        return result;
    }
}
