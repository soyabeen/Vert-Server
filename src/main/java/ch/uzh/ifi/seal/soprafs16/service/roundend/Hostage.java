package ch.uzh.ifi.seal.soprafs16.service.roundend;

import ch.uzh.ifi.seal.soprafs16.constant.LootType;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Loot;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devuser on 15.05.2016.
 */
public class Hostage implements RoundEnd {

    /**
     * All players positioned on the locomotive (pos 0) get an additional loot with value 250.
     *
     * @param game
     * @return
     */
    @Override
    public List<Positionable> execute(Game game) {
        List<Positionable> result = new ArrayList<>();
        for (Player player : game.getPlayers()) {
            if (0 == player.getCar()) {
                player.addLoot(new Loot(LootType.PURSE_SMALL, game.getId(), LootType.PURSE_SMALL.value()));
                result.add(player);
            }
        }
        return result;
    }
}
