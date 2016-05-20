package ch.uzh.ifi.seal.soprafs16.service.roundend;

import ch.uzh.ifi.seal.soprafs16.constant.LootType;
import ch.uzh.ifi.seal.soprafs16.model.*;
import ch.uzh.ifi.seal.soprafs16.utils.TargetFinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * all players on the roof of the waggon where the marshal is lose purse loot
 * if no purse loot on player he looses nothing
 * <p>
 * Created by devuser on 15.05.2016.
 */
public class MarshalsRevenge implements RoundEnd {


    @Override
    public List<Positionable> execute(Game game) {
        List<Positionable> temp = new ArrayList<>();
        List<Positionable> result = new ArrayList<>();

        TargetFinder targetFinder = new TargetFinder();
        Marshal marshal = new Marshal(game.getPositionMarshal());
        List<Player> victims = targetFinder.filterPlayersOnSameCar(marshal, game.getPlayers());

        for (Player victim : victims) {
            if (!victim.getLoots().isEmpty()) {
                temp = lootToRemove(victim.getLoots());

                if(!temp.isEmpty()) {
                    Loot tempLoot = (Loot) temp.get(0);
                    victim.dropLoot(tempLoot);
                    Loot lostLoot = new Loot(tempLoot.getType(), game.getId(),
                            tempLoot.getValue(), victim.getCar(), victim.getLevel());
                    result.add( lostLoot );
                }
            }
        }

        return result;
    }

    private List<Positionable> lootToRemove(List<Loot> playerLoot) {
        List<Positionable> victimLoot = new ArrayList<>();

        // Check if victim has a purse
        for (Loot lootItem : playerLoot) {
            if (lootItem.getType().equals(LootType.PURSE_SMALL)
                    || lootItem.getType().equals(LootType.PURSE_BIG)) {
                victimLoot.add(lootItem);
            }
        }

        // sort the loot list
        Collections.sort(victimLoot, new Comparator<Positionable>() {
            @Override
            public int compare(Positionable o1, Positionable o2) {
                if (((Loot) o1).getValue() < ((Loot) o2).getValue()) {
                    return -1;
                } else if (((Loot) o1).getValue() > ((Loot) o2).getValue()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        return victimLoot;
    }
}
