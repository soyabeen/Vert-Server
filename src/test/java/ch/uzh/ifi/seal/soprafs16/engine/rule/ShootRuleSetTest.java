package ch.uzh.ifi.seal.soprafs16.engine.rule;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.helper.PositionedPlayer;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
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
}
