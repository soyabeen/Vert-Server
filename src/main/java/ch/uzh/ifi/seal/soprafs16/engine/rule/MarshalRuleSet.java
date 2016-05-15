package ch.uzh.ifi.seal.soprafs16.engine.rule;

import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.engine.rule.exec.FindMarshalTargets;
import ch.uzh.ifi.seal.soprafs16.engine.rule.exec.MovePlayerExecRule;
import ch.uzh.ifi.seal.soprafs16.engine.rule.replace.MarshalRepRule;
import ch.uzh.ifi.seal.soprafs16.engine.rule.sim.MovePlayerBottomSimRule;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Marshal;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devuser on 08.05.2016.
 */
public class MarshalRuleSet extends RuleSet {

    private static final Logger logger = LoggerFactory.getLogger(MarshalRuleSet.class);

    public List<Positionable> simulate(Game game, Marshal marshal) {
        MovePlayerBottomSimRule simRule = new MovePlayerBottomSimRule(game.getNrOfCars());

        List<Positionable> result = new ArrayList<>();
        if (simRule.evaluate(marshal)) {
            result.addAll(simRule.simulate(marshal));
        }
        return result;
    }

    @Override
    public List<Positionable> simulate(Game game, Positionable player) {
        Marshal marshal = new Marshal(game.getPositionMarshal());

        return simulate(game, marshal);
    }

    @Override
    public List<Positionable> execute(ActionCommand command) {
        List<Positionable> result = new ArrayList<>();
        MovePlayerExecRule playerMove = new MovePlayerExecRule();
        FindMarshalTargets marshalTargets = new FindMarshalTargets();
        Game tmpGame = command.getGame();

        logger.debug("eval: " + marshalTargets.evaluate(command));

        // marshal move
        if (playerMove.evaluate(command)) {
            result.addAll(playerMove.execute(command));

            // when marshal moved, update his position in game
            // TODO: refactor!
            tmpGame.setPositionMarshal( result.get(0).getCar() );
            MarshalRepRule marshalReplace = new MarshalRepRule(tmpGame);

            // marshal move consequence
            result.addAll(marshalReplace.replace( marshalTargets.execute(command) ));
        }

        logger.debug("res: " + result.size());
        return result;
    }
}
