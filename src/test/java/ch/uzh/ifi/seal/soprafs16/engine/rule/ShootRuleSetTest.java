package ch.uzh.ifi.seal.soprafs16.engine.rule;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.constant.Character;
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
public class ShootRuleSetTest {

    @Test
    public void simFindTargetsToShoot() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {

        Player actor = PositionedPlayer.builder()
                .withUserName("actor-1b")
                .onLowerLevelAt(1).build();

        Player target1 = PositionedPlayer.builder()
                .withUserName("target-2b")
                .onLowerLevelAt(2).build();

        Player target2 = PositionedPlayer.builder()
                .withUserName("target-0b")
                .onLowerLevelAt(0).build();

        Player noTarget = PositionedPlayer.builder()
                .withUserName("NoTarget-1b")
                .onLowerLevelAt(1).build();

        Game game = new Game();
        game.setNrOfCars(4);
        game.addPlayer(actor);
        game.addPlayer(target1);
        game.addPlayer(target2);
        game.addPlayer(noTarget);

        RuleSet mrs = RuleSet.createRuleSet(CardType.FIRE);

        List<Positionable> resultList = mrs.simulate(game, actor);
        Assert.assertThat(resultList.size(), is(2));
    }

    @Test
    public void simFindTargetsToShootFilteredBelle() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {

        Player actor = PositionedPlayer.builder()
                .withUserName("actor-1b")
                .onLowerLevelAt(1).build();

        Player target1 = PositionedPlayer.builder()
                .withUserName("target-2b")
                .onLowerLevelAt(2).build();

        Player target2 = PositionedPlayer.builder()
                .withUserName("target-0b")
                .onLowerLevelAt(0).build();

        Player belle = PositionedPlayer.builder()
                .withUserName("belle-0b")
                .onLowerLevelAt(0).build();
        belle.setCharacter(Character.BELLE);

        Player noTarget = PositionedPlayer.builder()
                .withUserName("NoTarget-1b")
                .onLowerLevelAt(1).build();

        Game game = new Game();
        game.setNrOfCars(4);
        game.addPlayer(actor);
        game.addPlayer(target1);
        game.addPlayer(target2);
        game.addPlayer(belle);
        game.addPlayer(noTarget);

        RuleSet mrs = RuleSet.createRuleSet(CardType.FIRE);

        List<Positionable> resultList = mrs.simulate(game, actor);
        Assert.assertThat(resultList.size(), is(2));
    }

    @Test
    public void execShootTarget() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {

        Player actor = PositionedPlayer.builder()
                .withUserName("actor-1b")
                .onLowerLevelAt(1).build();

        Player target = PositionedPlayer.builder()
                .withUserName("target-2b")
                .onLowerLevelAt(2).build();
        target.setDeck(new ArrayList<Card>());


        Game game = new Game();
        game.setNrOfCars(4);
        game.addPlayer(actor);
        game.addPlayer(target);

        ActionCommand command = new ActionCommand(CardType.FIRE, game, actor, target);

        RuleSet mrs = RuleSet.createRuleSet(CardType.FIRE);
        List<Positionable> resultList = mrs.execute(command);
        Assert.assertThat(resultList.size(), is(2));
        Player shoots = null;
        Player gotShot = null;
        for (Positionable pos : resultList) {
            if(actor.getUsername().equals(((Player)pos).getUsername())) {
                shoots = (Player) pos;
            }else{
                gotShot = (Player) pos;
            }
        }
        Assert.assertEquals("Got shot must have username target-...", target.getUsername(), gotShot.getUsername());
        Assert.assertThat("Target must have new bullet card.", target.getDeck().size(), is(1));
        Assert.assertThat("Actor has -1 bullet.", shoots.getBullets(), is(5));

    }
}
