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
public class Pickpocketing implements RoundEnd {

    @Override
    public List<Positionable> execute(Game game) {
        // all players which are alone can pick up loot (if there is any)
        List<Positionable> result = new ArrayList<>();

        TargetFinder targetFinder = new TargetFinder();
        List<Player> allPlayers = game.getPlayers();
        List<Player> playersOnSameFloor = new ArrayList<>();

        // iterate through all players and check if the player is alone
        for(Player player : allPlayers) {
            playersOnSameFloor.addAll( targetFinder.filterPlayersOnSameFloor(player, allPlayers) );

            //if current player is alone
            if(playersOnSameFloor.isEmpty()) {
                result.add(player);
            }
        }

        return result;
    }
}
