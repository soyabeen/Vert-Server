package ch.uzh.ifi.seal.soprafs16.engine.rule;

import ch.qos.logback.core.net.SyslogOutputStream;
import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.helper.PositionedPlayer;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.hamcrest.core.IsNot;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;

/**
 * Created by soyabeen on 21.04.16.
 */
public class MoveRuleSetTest {

    @Test
    public void simForBotPlayerMovesOnlyOneCars() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        RuleSet mrs = RuleSet.createRuleSet(CardType.MOVE);
        Game game = new Game();
        game.setNrOfCars(3);

        Player player = new Player();
        player.setCar(1);
        player.setLevel(Positionable.Level.BOTTOM);

        List<Positionable> result = mrs.simulate(game, player);
        Assert.assertThat(result.size(), is(2));
        List<Positionable> levelCheck = new ArrayList<>();
        for (Positionable pos : result) {
            if (Positionable.Level.BOTTOM == pos.getLevel()) {
                levelCheck.add(pos);
            }
        }
        Assert.assertThat(levelCheck.size(), is(2));
    }

    @Test
    public void execForBotPlayerMovesOnlyOneCars() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        RuleSet mrs = RuleSet.createRuleSet(CardType.MOVE);
        Game game = new Game();
        game.setNrOfCars(3);

        Player player = new Player();
        player.setUsername("original player");
        player.setCar(2);
        player.setLevel(Positionable.Level.BOTTOM);

        Player target = new Player();
        target.setCar(1);
        target.setLevel(Positionable.Level.BOTTOM);

        ActionCommand ac = new ActionCommand(CardType.MOVE, game, player, target);

        List<Positionable> resultList = mrs.execute(ac);
        Assert.assertThat(resultList.size(), is(1));
        Player result = (Player) resultList.get(0);
        Assert.assertThat(result.getUsername(), is("original player"));
        Assert.assertThat(result.getCar(), is(target.getCar()));
        Assert.assertThat(result.getLevel(), is(target.getLevel()));
    }

    @Test
    public void handleMarshalCaseAfterMove() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        RuleSet mrs = RuleSet.createRuleSet(CardType.MOVE);
        Game game = new Game();
        game.setNrOfCars(3);

        Player player = PositionedPlayer.builder()
                .withUserName("original player")
                .onLowerLevelAt(1).build();

        Player target = PositionedPlayer.builder()
                .onLowerLevelAt(0).build();

        Player expected = PositionedPlayer.builder()
                .onUpperLevelAt(0).build();


        ActionCommand ac = new ActionCommand(CardType.MOVE, game, player, target);

        List<Positionable> result = mrs.execute(ac);
        Assert.assertThat(result.size(), is(1));
        Player resultPlayer = (Player) result.get(0);
        Assert.assertThat(resultPlayer.getUsername(), is("original player"));
        Assert.assertSame(resultPlayer.getCar(), expected.getCar());
        Assert.assertThat(resultPlayer.getLevel(), is(expected.getLevel()));
    }
}
