package ch.uzh.ifi.seal.soprafs16.service.roundend;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.constant.LootType;
import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.engine.rule.exec.RobberyExecRule;
import ch.uzh.ifi.seal.soprafs16.engine.rule.sim.RobberySimRule;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Loot;
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

        // iterate through all players and check if the player is alone
        for(Player player : allPlayers) {
            if (targetFinder.filterPlayersOnSameFloor(player, allPlayers).isEmpty()) {
                RobberySimRule simRule = new RobberySimRule(game);
                RobberyExecRule execRule = new RobberyExecRule();
                List<Loot> loots = castToLoot(simRule.simulate(player));
                for (Loot l: loots) {
                    if (l.getType().equals(LootType.PURSE_BIG) ||
                            l.getType().equals(LootType.PURSE_SMALL)) {
                        ActionCommand actionCommand = new ActionCommand(CardType.ROBBERY, game,
                                player, null);
                        actionCommand.setTargetLoot(l);
                        result.addAll(execRule.execute(actionCommand));
                    }
                }

            }
        }

        return result;
    }

    private List<Loot> castToLoot (List<Positionable> positionables) {
        List<Loot> loots = new ArrayList<>();
        for(Positionable p: positionables) {
            loots.add((Loot) p);
        }
        return loots;
    }
}
