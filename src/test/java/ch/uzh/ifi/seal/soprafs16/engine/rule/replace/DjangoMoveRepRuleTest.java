package ch.uzh.ifi.seal.soprafs16.engine.rule.replace;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.engine.rule.ShootRuleSet;
import ch.uzh.ifi.seal.soprafs16.helper.PositionedPlayer;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;

/**
 * Created by soyabeen on 18.05.16.
 */
public class DjangoMoveRepRuleTest {

    @Test
    public void checkEval() {

        Player notDjango = PositionedPlayer.builder()
                .withUserName("notDjango")
                .onLowerLevelAt(0)
                .id(1L)
                .build();

        Player target = PositionedPlayer.builder()
                .withUserName("target")
                .onLowerLevelAt(1)
                .id(2L)
                .build();

        ArrayList<Positionable> actors = new ArrayList<>();
        actors.add(notDjango);
        actors.add(target);

        Game game = new Game();
        game.setNrOfCars(3);

        ActionCommand command = new ActionCommand(CardType.FIRE, game, notDjango, target);
        DjangoMoveRepRule repRule = new DjangoMoveRepRule(command);
        List<Positionable> result = repRule.replace(actors);

        Assert.assertThat("Result list has size 2.", result.size(), is(2));
        Player resNot = null;
        Player resTarget = null;
        for (Positionable pos : result) {
            if (pos instanceof Player) {
                Player p = (Player) pos;
                if (p.getId().equals(1L)) {
                    resNot = p;
                }
                if (p.getId().equals(2L)) {
                    resTarget = p;
                }
            }
        }
        Assert.assertNotNull("NonDjango not null", resNot);
        Assert.assertEquals("NonDjango still on car 0", 0, resNot.getCar());
        Assert.assertNotNull("Target not null", resTarget);
        Assert.assertEquals("Target still on car 1", 1, resTarget.getCar());
        Assert.assertEquals("Target still on lower level.", Positionable.Level.BOTTOM, resTarget.getLevel());
    }

    @Test
    public void moveDjangosTargetByOne() {

        Player django = PositionedPlayer.builder()
                .withUserName("django")
                .onLowerLevelAt(0)
                .id(1L)
                .build();
        django.setCharacter(Character.DJANGO);

        Player target = PositionedPlayer.builder()
                .withUserName("target")
                .onLowerLevelAt(1)
                .id(2L)
                .build();

        ArrayList<Positionable> actors = new ArrayList<>();
        actors.add(django);
        actors.add(target);

        Game game = new Game();
        game.setNrOfCars(3);

        ActionCommand command = new ActionCommand(CardType.FIRE, game, django, target);
        DjangoMoveRepRule repRule = new DjangoMoveRepRule(command);
        List<Positionable> result = repRule.replace(actors);

        Assert.assertThat("Result list has size 2.", result.size(), is(2));
        Player resDjango = null;
        Player resTarget = null;
        for (Positionable pos : result) {
            if (pos instanceof Player) {
                Player p = (Player) pos;
                if (p.getId().equals(1L) && Character.DJANGO.equals(p.getCharacter())) {
                    resDjango = p;
                }
                if (p.getId().equals(2L)) {
                    resTarget = p;
                }
            }
        }
        Assert.assertNotNull("Django not null", resDjango);
        Assert.assertNotNull("Target not null", resTarget);
        Assert.assertEquals("Target now on car 2.", 2, resTarget.getCar());
        Assert.assertEquals("Target now on level BOTTOM.", Positionable.Level.BOTTOM, resTarget.getLevel());
    }

    @Test
    public void testDjangoShootsTargetsOnBeginningAndEndOfTrain() {
        Game game = new Game();
        game.setNrOfCars(3);

        Player django = PositionedPlayer.builder()
                .withUserName("django")
                .onLowerLevelAt(1)
                .id(1L)
                .build();
        django.setCharacter(Character.DJANGO);

        Player target1 = PositionedPlayer.builder()
                .withUserName("target1")
                .onLowerLevelAt(0)
                .id(2L)
                .build();

        Player target2 = PositionedPlayer.builder()
                .withUserName("target2")
                .onLowerLevelAt(game.getNrOfCars())
                .id(3L)
                .build();

        ArrayList<Positionable> actors = new ArrayList<>();
        actors.add(django);
        actors.add(target1);
        actors.add(target2);


        // Shoot Target 1
        ActionCommand command = new ActionCommand(CardType.FIRE, game, django, target1);
        ShootRuleSet shootRule = new ShootRuleSet();
        List<Positionable> result = shootRule.execute(command);

        Assert.assertThat("Result list has size 2.", result.size(), is(2));
        Player resDjango = (Player) filterResultList(result).get(0);
        Player resTarget1 = (Player) filterResultList(result).get(1);

        // Shoot Target 2
        command = new ActionCommand(CardType.FIRE, game, django, target2);
        shootRule = new ShootRuleSet();
        result = shootRule.execute(command);

        Player resTarget2 = (Player) filterResultList(result).get(1);

        Assert.assertNotNull("Django not null", resDjango);
        Assert.assertNotNull("Target1 not null", resTarget1);
        Assert.assertEquals("Target1 still on car 0.", 0, resTarget1.getCar());
        Assert.assertEquals("Target1 on level TOP because of Marshal.", Positionable.Level.TOP, resTarget1.getLevel());
        Assert.assertEquals("Target1 got shot.", resTarget1.getInjuries(), 2);

        Assert.assertNotNull("Target2 not null", resTarget2);
        Assert.assertEquals("Target2 still on car " + game.getNrOfCars(), 3, resTarget2.getCar());
        Assert.assertEquals("Target2 on level BOTTOM.", Positionable.Level.BOTTOM, resTarget2.getLevel());
        Assert.assertEquals("Target2 got shot.", resTarget2.getInjuries(), 1);
    }

    private List<Positionable> filterResultList(List<Positionable> positionables) {
        List<Positionable> result = new ArrayList<>();

        for (Positionable pos : positionables) {
            if (pos instanceof Player) {
                Player p = (Player) pos;
                if (p.getId().equals(1L) && Character.DJANGO.equals(p.getCharacter())) {
                    result.add(p);
                }
            }
        }

        for (Positionable pos : positionables) {
            if (pos instanceof Player) {
                Player p = (Player) pos;
                if (p.getId().equals(2L) || p.getId().equals(3L)) {
                    result.add(p);
                }
            }
        }

        return result;
    }

}
