package ch.uzh.ifi.seal.soprafs16.engine.rule.exec;

import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.List;

/**
 * Created by soyabeen on 22.04.16.
 */
public interface ChainableRule {

    public List<Positionable> chain(ExecutionRule rule, List<Positionable> pos);
}
