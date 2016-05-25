package ch.uzh.ifi.seal.soprafs16.engine.rule.exec;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.constant.Direction;
import ch.uzh.ifi.seal.soprafs16.constant.LootType;
import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.helper.PlayerBuilder;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Loot;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;

/**
 * Created by devuser on 20.05.2016.
 */
public class PunchExecRuleTest {

    @Test
    public void testEvaluatePunch() {
        List<Positionable> result = new ArrayList<>();
        PunchExecRule punchRule = new PunchExecRule();
        PlayerBuilder playerBuilder = new PlayerBuilder();

        Player attacker = playerBuilder.init().addCharacter(Character.DJANGO).build();
        Player victim = playerBuilder.init().addCharacter(Character.GHOST).build();

        Game game = new Game();
        game.addPlayer(attacker);
        game.addPlayer(victim);

        ActionCommand ac = new ActionCommand(CardType.PUNCH, game, attacker, victim);
        Assert.assertTrue(punchRule.evaluate(ac));
    }

    @Test
    public void testExecutePunch() {
        List<Positionable> result = new ArrayList<>();
        PunchExecRule punchRule = new PunchExecRule();
        PlayerBuilder playerBuilder = new PlayerBuilder();

        Game game = new Game();
        Loot l1 = new Loot(LootType.JEWEL, game.getId(), 1000);
        l1.setId(1L);

        Player attacker = playerBuilder.init().addCharacter(Character.DJANGO).build();
        attacker.setCar(1);
        attacker.setLevel(Positionable.Level.BOTTOM);

        Player victim = playerBuilder.init().addCharacter(Character.GHOST).build();
        victim.addLoot(l1);
        victim.setCar(1);
        victim.setLevel(Positionable.Level.BOTTOM);

        game.setNrOfCars(4);
        game.addPlayer(attacker);
        game.addPlayer(victim);

        ActionCommand ac = new ActionCommand(CardType.PUNCH, game, attacker, victim);
        ac.setTargetLoot(l1);
        ac.setDirection(Direction.TO_TAIL);

        result = punchRule.execute(ac);
        Assert.assertThat(result.size(), is(2));

        Assert.assertTrue(result.get(0) instanceof Loot);
        Assert.assertThat(result.get(1).getCar(), is(2));
        Assert.assertTrue(( (Player) result.get(1)).getLoots().isEmpty());
        Assert.assertThat("Victim is injured.", ((Player) result.get(1)).getBrokenNoses(), is(1));
    }
}

