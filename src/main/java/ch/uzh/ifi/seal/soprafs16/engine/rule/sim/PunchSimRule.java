package ch.uzh.ifi.seal.soprafs16.engine.rule.sim;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import ch.uzh.ifi.seal.soprafs16.utils.TargetFinder;
import org.hibernate.cfg.NotYetImplementedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devuser on 09.05.2016.
 */
public class PunchSimRule implements SimulationRule {
    
    private Game game;
    private TargetFinder targetFinder;
    
    public PunchSimRule(Game game) {
        this.game = game;
        this.targetFinder = new TargetFinder();
    }

    @Override
    public boolean evaluate(Positionable actor) {
        return !(targetFinder.filterPlayersOnSameCar(actor, game.getPlayers()).isEmpty());
    }

    @Override
    public List<Positionable> simulate(Positionable actor) {
        return new ArrayList<Positionable>( targetFinder.filterPlayersOnSameCar(actor, game.getPlayers()) );
    }
}
