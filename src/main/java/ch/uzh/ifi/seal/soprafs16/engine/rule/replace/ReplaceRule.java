package ch.uzh.ifi.seal.soprafs16.engine.rule.replace;

import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.engine.rule.exec.ExecutionRule;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.List;

/**
 * Repace rules take a list of positionables and replace that list with an other list.
 * <p/>
 * Created by soyabeen on 27.04.16.
 */
public interface ReplaceRule {

    public boolean evaluate(List<Positionable> actors);

    public List<Positionable> replace(List<Positionable> actors);

}
