package ch.uzh.ifi.seal.soprafs16.engine.rule;

import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.engine.rule.exec.PunchExecRule;
import ch.uzh.ifi.seal.soprafs16.engine.rule.exec.MovePlayerExecRule;
import ch.uzh.ifi.seal.soprafs16.engine.rule.filter.BelleNoTargetFilterRule;
import ch.uzh.ifi.seal.soprafs16.engine.rule.replace.CheyennePunchRepRule;
import ch.uzh.ifi.seal.soprafs16.engine.rule.replace.MarshalRepRule;
import ch.uzh.ifi.seal.soprafs16.engine.rule.sim.PunchSimRule;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devuser on 09.05.2016.
 */
public class PunchRuleSet extends RuleSet {

    @Override
    public List<Positionable> simulate(Game game, Positionable player) {
        PunchSimRule punch = new PunchSimRule(game);
        BelleNoTargetFilterRule belle = new BelleNoTargetFilterRule();

        return belle.filter(punch.simulate(player));
    }

    @Override
    public List<Positionable> execute(ActionCommand command) {
        List<Positionable> result = new ArrayList<>();
        PunchExecRule punchPlayer = new PunchExecRule();
        MarshalRepRule marshalRepRule = new MarshalRepRule(command.getGame());
        CheyennePunchRepRule cheyenne = new CheyennePunchRepRule(command);
        
        return marshalRepRule.replace(cheyenne.replace(punchPlayer.execute(command)));
    }

}
