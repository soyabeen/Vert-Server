package ch.uzh.ifi.seal.soprafs16.service.roundend;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.engine.rule.MarshalRuleSet;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Marshal;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devuser on 15.05.2016.
 */
public class AngryMarshal implements RoundEnd{

    @Override
    public List<Positionable> execute(Game game) {
        List<Positionable> result = new ArrayList<>();

        // all players on the roof of the waggon where the marshal is in get a bullet card
        for (Positionable player : game.getPlayers()) {
            if(player.getLevel() == Positionable.Level.TOP
                    && player.getCar() == game.getPositionMarshal()) {
                ((Player) player).getsShot();
                result.add(player);
            }
        }

        //Move marshal to end of train
        Marshal current = new Marshal(game.getPositionMarshal());
        Marshal target = new Marshal(game.getPositionMarshal() + 1);

        ActionCommand ac = new ActionCommand(CardType.MARSHAL, game, current, target);
        MarshalRuleSet mrs = new MarshalRuleSet();
        result.addAll( new ArrayList<Positionable>(mrs.execute(ac)) );

        return result;
    }
}
