package ch.uzh.ifi.seal.soprafs16.engine.rule.sim;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.hibernate.cfg.NotYetImplementedException;

import java.util.List;

/**
 * Created by devuser on 09.05.2016.
 */
public class PunchSimRule implements SimulationRule {
    
    private Game game;
    
    public PunchSimRule(Game game) {
        this.game = game;
    }

    private boolean isOnSameFloor(Positionable attacker, Positionable victim) {
        return attacker.getCar() == victim.getCar() && attacker.getLevel() == victim.getLevel();
    }

    private List<Positionable> findTargets(Positionable attacker) {
        // use TargetFinder.java from utils
        // check if victim is cheyenne
        throw new NotYetImplementedException("Implement findTargets() in PunchSimRule");
    }

    @Override
    public boolean evaluate(Positionable actor) {
        return !(findTargets(actor).isEmpty());
    }

    @Override
    public List<Positionable> simulate(Positionable actor) {
        return findTargets(actor);
    }
}
