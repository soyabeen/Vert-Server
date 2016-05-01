package ch.uzh.ifi.seal.soprafs16.engine.rule.exec;

import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.engine.rule.RuleUtils;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soyabeen on 27.04.16.
 */
public class FloorChangeExecRule implements ExecutionRule {

    @Override
    public boolean evaluate(ActionCommand command) {
        // Floor chane always possible.
        return true;
    }

    @Override
    public List<Positionable> execute(ActionCommand command) {
        List<Positionable> result = new ArrayList<>();

        Player newState = command.getCurrentPlayer();
        result.add(RuleUtils.swapLevel(newState));

        return result;
    }
}
