package ch.uzh.ifi.seal.soprafs16.engine.rule.replace;

import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.engine.rule.exec.ExecutionRule;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.List;

/**
 * Created by soyabeen on 27.04.16.
 */
public interface ReplaceRule {

    public boolean evaluate(List<Positionable> actors);

    public List<Positionable> replace(List<Positionable> actors);

    /**
     * Created by soyabeen on 20.04.16.
     */
    class CheckMarshalExecRule implements ExecutionRule {

        @Override
        public boolean evaluate(ActionCommand command) {
            return false;
        }

        @Override
        public List<Positionable> execute(ActionCommand command) {
            return null;
        }
    }
}
