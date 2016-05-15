package ch.uzh.ifi.seal.soprafs16.engine.rule;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.helper.PlayerBuilder;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Marshal;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.hamcrest.core.Is.is;

/**
 * Created by devuser on 12.05.2016.
 */
public class MarshalRuleSetTest {

    @Test
    public void simForMarshalMovesOnlyOneCar() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        RuleSet mrs = RuleSet.createRuleSet(CardType.MARSHAL);
        Game game = new Game();
        game.setNrOfCars(3);

        // Test Marshal move from starting car
        game.setPositionMarshal(0);
        Marshal marshal = new Marshal(game.getPositionMarshal());

        List<Positionable> result = mrs.simulate(game, marshal);
        Assert.assertThat(result.size(), is(1));

        Assert.assertThat("Marshal is in car 1.", result.get(0).getCar(), is(1));

        // Test Marshal moves from middle car
        game.setPositionMarshal(1);
        marshal = new Marshal(game.getPositionMarshal());
        result = mrs.simulate(game, marshal);
        Assert.assertThat("There are 2 simulated Marshals.", result.size(), is(2));

        Assert.assertThat("Emulated Marshal 1 is in car 0.", result.get(0).getCar(), is(0));
        Assert.assertThat("Emulated Marshal 2 is in car 3.", result.get(1).getCar(), is(2));
    }

    @Test
    public void execMoveForMarshalInLocomotive() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        RuleSet mrs = RuleSet.createRuleSet(CardType.MARSHAL);
        Game game = new Game();
        game.setNrOfCars(3);
        game.setPositionMarshal(0);

        Marshal current;
        Marshal target;
        ActionCommand ac;

        // Marshal in Locomotive moving 1 to TAIL
        current = new Marshal(0);
        target = new Marshal(1);
        ac = new ActionCommand(CardType.MARSHAL, game, current, target);

        List<Positionable> result = mrs.execute(ac);
        Assert.assertThat(result.size(), is(1));

        Assert.assertThat(result.get(0).getCar(), is(1));

        // Marshal in middle waggon moving 1 to TAIL
        // + Player in target waggon
        PlayerBuilder playerbuilder = new PlayerBuilder();
        Player player = playerbuilder.init().getPlayerNoPersistence(Character.BELLE);
        player.setCar(2);
        player.setLevel(Positionable.Level.BOTTOM);
        game.addPlayer(player);

        // Updates
        game.setPositionMarshal(1);

        current = new Marshal(1);
        target = new Marshal(2);
        ac = new ActionCommand(CardType.MARSHAL, game, current, target);

        result = mrs.execute(ac);
        Assert.assertThat(result.size(), is(2));

        // Update game after rule
        Assert.assertThat(result.get(0).getCar(), is(2));
        Assert.assertEquals(result.get(1).getLevel(), Positionable.Level.TOP);

        // Marshal in last waggon moving outside of waggon
        current = new Marshal(2);
        target = new Marshal(3);
        ac = new ActionCommand(CardType.MARSHAL, game, current, target);

        result = mrs.execute(ac);
        Assert.assertThat(result.size(), is(0));


    }
}
