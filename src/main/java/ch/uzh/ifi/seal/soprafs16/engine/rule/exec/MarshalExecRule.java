package ch.uzh.ifi.seal.soprafs16.engine.rule.exec;

import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.engine.rule.RuleUtils;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import ch.uzh.ifi.seal.soprafs16.utils.TargetFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devuser on 08.05.2016.
 */
public class MarshalExecRule implements ExecutionRule {

    private static final Logger logger = LoggerFactory.getLogger(MovePlayerExecRule.class);

    private TargetFinder targetFinder;

    public MarshalExecRule() {
        targetFinder = new TargetFinder();
    }


    @Override
    public boolean evaluate(ActionCommand command) {
        return RuleUtils.isPlacedOnTrain(command.getTargetPlayer(), command.getGame().getNrOfCars());
    }

    @Override
    public List<Positionable> execute(ActionCommand command) {
        Positionable marshal = command.getTargetPlayer();
        List<Positionable> result = new ArrayList<>();
        result.addAll( targetFinder.filterPlayersOnSameFloor(marshal, command.getGame().getPlayers()) );
        return result;
    }

}
