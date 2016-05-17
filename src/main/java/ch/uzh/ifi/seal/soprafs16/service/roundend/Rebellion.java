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
    public List<Positionable> execute(Game game, List<Positionable> players) {
        // all players inside the train get a bullet card
        List<Positionable> result = new ArrayList<>();

        TargetFinder targetFinder = new TargetFinder();
        Marshal marshal = new Marshal(game.getPositionMarshal());
        List<Player> playersInsideTrain = new ArrayList<>();
        // all players on same floor as marshal (= inside of train)
        playersInsideTrain.addAll( targetFinder.filterPlayersOnSameFloor(marshal, game.getPlayers()) );

        for(Player target : playersInsideTrain) {
            target.getsShot();
            result.add(target);
        }

        return result;
    }
}
