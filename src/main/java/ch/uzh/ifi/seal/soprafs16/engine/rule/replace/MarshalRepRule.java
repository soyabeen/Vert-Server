package ch.uzh.ifi.seal.soprafs16.engine.rule.replace;

import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.jboss.logging.annotations.Pos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soyabeen on 27.04.16.
 */
public class MarshalRepRule implements ReplaceRule {

    private Positionable swapLevel(Positionable actor) {
        if (Positionable.Level.TOP == actor.getLevel()) {
            actor.setLevel(Positionable.Level.BOTTOM);
        } else {
            actor.setLevel(Positionable.Level.TOP);
        }
        return actor;
    }

    private boolean isOnSameFloorAsMarshal(Positionable actor) {
        // TODO: use real marshal position
        if (actor.getCar() == 0) {
            if (actor.getLevel() == Positionable.Level.BOTTOM) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean evaluate(List<Positionable> actors) {
        for (Positionable pos : actors) {
            if (isOnSameFloorAsMarshal(pos)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Positionable> replace(List<Positionable> actors) {
        List<Positionable> replaced = new ArrayList<>();
        for (Positionable pos : actors) {
            if (isOnSameFloorAsMarshal(pos)) {
                // TODO: Add bullet card to player shot by marshal.
                replaced.add(swapLevel(pos));
            } else {
                replaced.add(pos);
            }
        }
        return replaced;
    }
}
