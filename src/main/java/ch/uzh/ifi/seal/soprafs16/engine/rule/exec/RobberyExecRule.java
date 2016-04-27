package ch.uzh.ifi.seal.soprafs16.engine.rule.exec;

import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.List;

/**
 * Created by soyabeen on 27.04.16.
 */
public class RobberyExecRule implements ExecutionRule {

    @Override
    public boolean evaluate(ActionCommand command) {
        return false;
    }

    @Override
    public List<Positionable> execute(ActionCommand command) {
        return null;
    }
}
