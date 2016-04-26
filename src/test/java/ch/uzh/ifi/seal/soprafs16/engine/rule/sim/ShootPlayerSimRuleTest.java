package ch.uzh.ifi.seal.soprafs16.engine.rule.sim;

import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.helper.PlayerBuilder;
import ch.uzh.ifi.seal.soprafs16.helper.PositionedPlayer;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by soyabeen on 26.04.16.
 */
public class ShootPlayerSimRuleTest {

    @Test
    public void evalFalseWhenNoBulletsLeft() {
        Player actor = PositionedPlayer.builder().withUserName("Actor1-bottom").onLowerLevelAt(1).build();
        actor.setBullets(0);
        ActionCommand actionInfo = new ActionCommand(null, null, actor, null);

        ShootPlayerSimRule rule = new ShootPlayerSimRule(actionInfo);

        Assert.assertFalse("Expect false when no bullets left.", rule.evaluate(actor));
    }


    @Test
    public void evalTrueWhenNeighboringPlayerOnBottomLevel() {

        Game game = new Game();
        game.setName("TrueWhenNeighborBotGame");

        ArrayList<Player> players  = new ArrayList<>();
        players.add(PositionedPlayer.builder().withUserName("P2-bottom").onLowerLevelAt(2).build());

        Player actor = PositionedPlayer.builder().withUserName("Actor1-bottom").onLowerLevelAt(1).build();
        players.add(actor);

        game.setPlayers(players);

        ActionCommand actionInfo = new ActionCommand(null, game, actor, null);


        ShootPlayerSimRule rule = new ShootPlayerSimRule(actionInfo);

        Assert.assertTrue("Expect true for neighboring player on bottom level.", rule.evaluate(actor));
    }

    @Test
    public void evalFalseWhenNoNeighboringPlayerOnBottomLevel() {

        Game game = new Game();
        game.setName("FalseWhenNoNeighborBotGame");

        ArrayList<Player> players  = new ArrayList<>();
        players.add(PositionedPlayer.builder().withUserName("P3-bottom").onLowerLevelAt(3).build());

        Player actor = PositionedPlayer.builder().withUserName("Actor1-bottom").onLowerLevelAt(1).build();
        players.add(actor);

        game.setPlayers(players);

        ActionCommand actionInfo = new ActionCommand(null, game, actor, null);

        ShootPlayerSimRule rule = new ShootPlayerSimRule(actionInfo);

        Assert.assertFalse("Expect false for no neighboring player on bottom level.", rule.evaluate(actor));
    }
}
