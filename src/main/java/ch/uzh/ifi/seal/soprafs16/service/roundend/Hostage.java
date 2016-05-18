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

    @Override
    public List<Positionable> execute(Game game) {
        // all players get 250$ reward
        List<Positionable> result = new ArrayList<>();

        List<Player> allPlayers = game.getPlayers();
        for(Player player : allPlayers) {
            player.addLoot(new Loot(LootType.PURSE_SMALL, game.getId(), LootType.PURSE_SMALL.value()));
            result.add(player);
        }

        return result;
    }
}
