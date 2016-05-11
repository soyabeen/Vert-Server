package ch.uzh.ifi.seal.soprafs16.engine.rule.exec;

import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.List;

/**
 * Created by devuser on 09.05.2016.
 */
public class PunchExecRule implements ExecutionRule {
    @Override
    public boolean evaluate(ActionCommand command) {
        return false;
    }

    @Override
    public List<Positionable> execute(ActionCommand command) {
        return null;
    }
}
