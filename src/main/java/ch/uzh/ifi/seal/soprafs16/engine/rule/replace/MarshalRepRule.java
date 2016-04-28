package ch.uzh.ifi.seal.soprafs16.engine.rule.replace;

import ch.uzh.ifi.seal.soprafs16.engine.rule.RuleUtils;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.ArrayList;
import java.util.List;

/**
 * Players on the same floor as the marshal will be shoot and placed on the other floor of the car.
 * <p>
 * Created by soyabeen on 27.04.16.
 */
public class MarshalRepRule implements ReplaceRule {

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
                Player target = (Player) RuleUtils.swapLevel(pos);
                target.getsShot();
                replaced.add(target);
            } else {
                replaced.add(pos);
            }
        }
        return replaced;
    }
}
