package ch.uzh.ifi.seal.soprafs16.engine.rule;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.helper.PositionedPlayer;
import ch.uzh.ifi.seal.soprafs16.model.Card;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;

/**
 * Created by soyabeen on 27.04.16.
 */
public class FloorChangeRuleSetTest {

    @Test
    public void simFloorChangeBotToTop() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        RuleSet mrs = RuleSet.createRuleSet(CardType.FLOORCHANGE);
        Game game = new Game();
        game.setNrOfCars(3);

        Player player = PositionedPlayer.builder()
                .withUserName("actor")
                .onLowerLevelAt(2).build();

        List<Positionable> resultList = mrs.simulate(game, player);
        Assert.assertThat(resultList.size(), is(1));
        Player result = (Player) resultList.get(0);
        Assert.assertEquals("Kept username.", "actor", result.getUsername());
        Assert.assertEquals("Floor has changed.", Positionable.Level.TOP,  result.getLevel());
    }

    @Test
    public void simFloorChangeTopToBot() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        RuleSet mrs = RuleSet.createRuleSet(CardType.FLOORCHANGE);
        Game game = new Game();
        game.setNrOfCars(3);

        Player player = PositionedPlayer.builder()
                .withUserName("actor")
                .onUpperLevelAt(2).build();

        List<Positionable> resultList = mrs.simulate(game, player);
        Assert.assertThat(resultList.size(), is(1));
        Player result = (Player) resultList.get(0);
        Assert.assertEquals("Kept username.", "actor", result.getUsername());
        Assert.assertEquals("Floor has changed.", Positionable.Level.BOTTOM,  result.getLevel());
    }

    @Test
    public void execFloorChange() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        RuleSet mrs = RuleSet.createRuleSet(CardType.FLOORCHANGE);
        Game game = new Game();
        game.setNrOfCars(3);

        Player player = PositionedPlayer.builder()
                .withUserName("actor")
                .onLowerLevelAt(2).build();

        ActionCommand command = new ActionCommand(CardType.FLOORCHANGE, game, player, null);

        List<Positionable> resultList = mrs.execute(command);
        Assert.assertThat(resultList.size(), is(1));
        Player result = (Player) resultList.get(0);
        Assert.assertEquals("Kept username.", "actor", result.getUsername());
        Assert.assertEquals("Floor has changed.", Positionable.Level.TOP,  result.getLevel());
    }

    @Test
    public void execFloorChangeWithMarshal() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        RuleSet mrs = RuleSet.createRuleSet(CardType.FLOORCHANGE);
        Game game = new Game();
        game.setNrOfCars(3);

        Player player = PositionedPlayer.builder()
                .withUserName("actor")
                .onUpperLevelAt(0).build();
        player.setDeck(new ArrayList<Card>());

        ActionCommand command = new ActionCommand(CardType.FLOORCHANGE, game, player, null);

        List<Positionable> resultList = mrs.execute(command);
        Assert.assertThat(resultList.size(), is(1));
        Player result = (Player) resultList.get(0);
        Assert.assertEquals("Kept username.", "actor", result.getUsername());
        Assert.assertEquals("Floor is back as on start due to marshal.", Positionable.Level.TOP,  result.getLevel());
        Assert.assertThat("Player got shot by marshal", result.getDeck().size(), is(1));
    }

}
