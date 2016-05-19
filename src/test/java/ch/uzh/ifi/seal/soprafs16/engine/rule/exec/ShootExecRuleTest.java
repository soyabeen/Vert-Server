package ch.uzh.ifi.seal.soprafs16.engine.rule.exec;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.helper.PositionedPlayer;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by soyabeen on 19.05.16.
 */
public class ShootExecRuleTest {

    @Test
    public void checkEvalTrue() {
        Player shooter = PositionedPlayer.builder()
                .id(1L)
                .withUserName("shooter").build();

        Player target = PositionedPlayer.builder()
                .id(2L)
                .withUserName("target").build();

        ActionCommand command = new ActionCommand(CardType.FIRE, null, shooter, target);
        Assert.assertTrue(new ShootExecRule().evaluate(command));
    }

    @Test
    public void checkEvalFalse() {
        Player shooter = PositionedPlayer.builder()
                .id(1L)
                .withUserName("shooter").build();
        shooter.setBullets(0);

        Player target = PositionedPlayer.builder()
                .id(2L)
                .withUserName("target").build();

        ActionCommand command = new ActionCommand(CardType.FIRE, null, shooter, target);
        Assert.assertFalse(new ShootExecRule().evaluate(command));
    }
}
