package ch.uzh.ifi.seal.soprafs16.engine.rule.sim;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import ch.uzh.ifi.seal.soprafs16.utils.TargetFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soyabeen on 22.04.16.
 */
public class ShootPlayerSimRule implements SimulationRule {

    private static final Logger logger = LoggerFactory.getLogger(ShootPlayerSimRule.class);

    //    private ActionCommand commandInfo;
    private Game game;
    private TargetFinder finder;

    public ShootPlayerSimRule(Game game) {
//        this.commandInfo = commandInfo;
        this.game = game;
        finder = new TargetFinder();
    }

    public boolean hasBulletLeft(Player player) {
        boolean res = player.getBullets() > 0;
        if (!res) {
            logger.debug("Player {} has no bullets left.", player);
        }
        return res;
    }

    public boolean seesATargetToShoot(Player actor, List<Player> players) {
        boolean res = !finder.findTargetToShoot(actor, players).isEmpty();
        if (!res) {
            logger.debug("Player {} has no targets to shoot.", actor);
        }
        return res;
    }

    @Override
    public boolean evaluate(Positionable actor) {
        Player player = (Player) actor;
        return hasBulletLeft(player)
                && seesATargetToShoot(player, game.getPlayers());
    }

    @Override
    public List<Positionable> simulate(Positionable actor) {
        Player player = (Player) actor;
        List<Positionable> result = new ArrayList<>();
        result.addAll(finder.findTargetToShoot(player, game.getPlayers()));
        return result;
    }
}
