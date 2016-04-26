package ch.uzh.ifi.seal.soprafs16.engine.rule.filter;

import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.List;

/**
 * Created by soyabeen on 27.04.16.
 */
public interface FilterRule {

    public boolean evaluate(List<Positionable> actors);

    public List<Positionable> filter(List<Positionable> actors);
}
