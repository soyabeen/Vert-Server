package ch.uzh.ifi.seal.soprafs16.engine.rule.filter;

import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soyabeen on 27.04.16.
 */
public class BelleNoTargetFilterRule implements FilterRule {

    @Override
    public boolean evaluate(List<Positionable> actors) {
        // Belles special ability only triggers if there are other possible targets.
        if (actors.size() <= 1) {
            return false;
        }
        return true;
    }

    @Override
    public List<Positionable> filter(List<Positionable> actors) {
        if (evaluate(actors)) {
            List<Positionable> filteredRes = new ArrayList<>();
            for (Positionable pos : actors) {
                if (Character.BELLE != ((Player) pos).getCharacter()) {
                    filteredRes.add(pos);
                }
            }
            return filteredRes;
        } else {
            return actors;
        }
    }
}
