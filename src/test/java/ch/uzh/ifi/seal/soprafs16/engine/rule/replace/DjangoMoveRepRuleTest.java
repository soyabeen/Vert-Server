package ch.uzh.ifi.seal.soprafs16.engine.rule.replace;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
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
}
