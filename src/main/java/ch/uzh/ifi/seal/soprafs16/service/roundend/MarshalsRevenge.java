package ch.uzh.ifi.seal.soprafs16.service.roundend;

import ch.uzh.ifi.seal.soprafs16.model.*;
import ch.uzh.ifi.seal.soprafs16.utils.TargetFinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by devuser on 15.05.2016.
 */
public class MarshalsRevenge implements RoundEnd {

    @Override
    public List<Positionable> execute(Game game, List<Positionable> players) {
        // all players on the roof of the waggon where the marshal is in lose purse loot
        // if no purse loot on player he looses nothing
        List<Positionable> result = new ArrayList<>();

        TargetFinder targetFinder = new TargetFinder();
        Marshal marshal = new Marshal(game.getPositionMarshal());
        List<Player> victims = targetFinder.filterPlayersOnSameCar(marshal, game.getPlayers());

        for(Player victim : victims){
            if(!victim.getLoots().isEmpty()) {
                result.add( lootToRemove(victim.getLoots()) );
            }
        }

        return result;
    }

    private Loot lootToRemove(List<Loot> playerLoot) {
        List<Loot> victimLoot = playerLoot;
        Collections.shuffle(victimLoot);
        return victimLoot.get(0);
    }
}
