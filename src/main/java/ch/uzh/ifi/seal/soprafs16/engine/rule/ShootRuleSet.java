package ch.uzh.ifi.seal.soprafs16.engine.rule;

import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.engine.rule.exec.ShootExecRule;
import ch.uzh.ifi.seal.soprafs16.engine.rule.filter.BelleNoTargetFilterRule;
import ch.uzh.ifi.seal.soprafs16.engine.rule.sim.ShootPlayerSimRule;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soyabeen on 20.04.16.
 */
public class ShootRuleSet extends RuleSet {

    private static final Logger logger = LoggerFactory.getLogger(ShootRuleSet.class);

    @Override
    public List<Positionable> simulate(Game game, Player player) {
        ShootPlayerSimRule shoot = new ShootPlayerSimRule(game);
        BelleNoTargetFilterRule belle = new BelleNoTargetFilterRule();

        return belle.filter(shoot.simulate(player));
    }

    @Override
    public List<Positionable> execute(ActionCommand command) {
        List<Positionable> result = new ArrayList<>();
        ShootExecRule shoot = new ShootExecRule();
        return shoot.execute(command);
    }
}
