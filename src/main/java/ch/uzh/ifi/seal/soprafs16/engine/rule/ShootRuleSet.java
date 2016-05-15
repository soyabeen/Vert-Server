package ch.uzh.ifi.seal.soprafs16.engine.rule;

import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.engine.rule.exec.ShootExecRule;
import ch.uzh.ifi.seal.soprafs16.engine.rule.filter.BelleNoTargetFilterRule;
import ch.uzh.ifi.seal.soprafs16.engine.rule.filter.TucoAdditionalTargetFilterRule;
import ch.uzh.ifi.seal.soprafs16.engine.rule.sim.ShootPlayerSimRule;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.List;

/**
 * Created by soyabeen on 20.04.16.
 */
public class ShootRuleSet extends RuleSet {

    @Override
    public List<Positionable> simulate(Game game, Positionable player) {
        ShootPlayerSimRule shoot = new ShootPlayerSimRule(game);
        BelleNoTargetFilterRule belle = new BelleNoTargetFilterRule();

        return belle.filter(shoot.simulate(player));
    }

    @Override
    public List<Positionable> execute(ActionCommand command) {
        ShootExecRule shoot = new ShootExecRule();
        TucoAdditionalTargetFilterRule tuco = new TucoAdditionalTargetFilterRule(command);
        return tuco.filter(shoot.execute(command));
    }
}
