package ch.uzh.ifi.seal.soprafs16.engine.rule.sim;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Loot;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soyabeen on 27.04.16.
 */
public class RobberySimRule implements SimulationRule {

    private Game game;

    public RobberySimRule(Game game) {
        this.game = game;
    }

    private boolean isOnSameFloor(Positionable actor, Positionable loot) {
        return actor.getCar() == loot.getCar() && actor.getLevel() == loot.getLevel();
    }

    private List<Positionable> findLootsOnFloorOfPlayer(Positionable actor, List<Loot> loots) {
        List<Positionable> lootsOnSameFloor = new ArrayList<>();
        for (Loot loot : loots) {
            if (isOnSameFloor(actor, loot)) {
                lootsOnSameFloor.add(loot);
            }
        }
        return lootsOnSameFloor;
    }

    @Override
    public boolean evaluate(Positionable actor) {
        return !findLootsOnFloorOfPlayer(actor, game.getLoots()).isEmpty();
    }

    @Override
    public List<Positionable> simulate(Positionable actor) {
        return findLootsOnFloorOfPlayer(actor, game.getLoots());
    }
}
