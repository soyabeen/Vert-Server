package ch.uzh.ifi.seal.soprafs16.engine.rule.replace;

import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.model.Loot;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devuser on 11.05.2016.
 */
public class CheyennePunchRepRule implements ReplaceRule {

    private ActionCommand command;

    public CheyennePunchRepRule(ActionCommand command) {
        this.command = command;
    }

    @Override
    public boolean evaluate(List<Positionable> actors) {
        return Character.CHEYENNE == ((Player) command.getCurrentPlayer()).getCharacter()
                && containsLoot(actors);
    }

    private boolean containsLoot(List<Positionable> actors) {
        for(Positionable elem : actors) {
            if(elem instanceof Loot) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Positionable> replace(List<Positionable> stuff) {

        if(evaluate(stuff)) {
            List<Positionable> result = new ArrayList<Positionable>();
            Player cheyenne = (Player) command.getCurrentPlayer();

            for(Positionable element : stuff) {
                if(element instanceof Loot) {
                    Loot el = (Loot) element;

                    el.setOwnerId(cheyenne.getId());
                    cheyenne.addLoot(el);

                    result.add(element);
                    result.add(cheyenne);
                } else {
                    result.add(element);
                }
            }

            return result;
        }

        return stuff;
    }
}
