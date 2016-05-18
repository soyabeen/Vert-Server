package ch.uzh.ifi.seal.soprafs16.engine.rule;

import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.engine.rule.exec.ShootExecRule;
import ch.uzh.ifi.seal.soprafs16.engine.rule.filter.BelleNoTargetFilterRule;
import ch.uzh.ifi.seal.soprafs16.engine.rule.filter.TucoAdditionalTargetFilterRule;
import ch.uzh.ifi.seal.soprafs16.engine.rule.replace.DjangoMoveRepRule;
import ch.uzh.ifi.seal.soprafs16.engine.rule.replace.MarshalRepRule;
import ch.uzh.ifi.seal.soprafs16.engine.rule.sim.ShootPlayerSimRule;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
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
        TucoAdditionalTargetFilterRule tuco = new TucoAdditionalTargetFilterRule(game, (Player) player);
        return belle.filter(tuco.filter(shoot.simulate(player)));
    }

    @Override
    public List<Positionable> execute(ActionCommand command) {
        ShootExecRule shoot = new ShootExecRule();
        DjangoMoveRepRule django = new DjangoMoveRepRule(command);
        MarshalRepRule marshal = new MarshalRepRule(command.getGame());
        return marshal.replace(django.replace(shoot.execute(command)));
    }
}
