package ch.uzh.ifi.seal.soprafs16.engine.rule.filter;

import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.helper.PositionedPlayer;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soyabeen on 19.05.16.
 */
public class TucoFilterRuleTest {

    @Test
    public void allPlayersOnTopMoveToLastCar() {

        int numberOfTrainCars = 3;

        Game game = new Game();
        game.setNrOfCars(numberOfTrainCars);

        Player p0b = PositionedPlayer.builder()
                .id(1L)
                .withUserName("loc-0-bot")
                .onLowerLevelAt(0).build();
        game.addPlayer(p0b);

        Player p0t = PositionedPlayer.builder()
                .id(2L)
                .withUserName("loc-0-top")
                .onUpperLevelAt(0).build();
        game.addPlayer(p0t);

        Player p1t = PositionedPlayer.builder()
                .id(3L)
                .withUserName("car-1-top")
                .onUpperLevelAt(1).build();
        game.addPlayer(p1t);

        Player p1b = PositionedPlayer.builder()
                .id(4L)
                .withUserName("car-1-bot")
                .onLowerLevelAt(1).build();
        game.addPlayer(p1b);

        Player tuco = PositionedPlayer.builder()
                .id(5L)
                .withUserName("Tuco")
                .onLowerLevelAt(1).build();
        tuco.setCharacter(Character.TUCO);
        game.addPlayer(tuco);

        TucoAdditionalTargetFilterRule tucoRule = new TucoAdditionalTargetFilterRule(game, tuco);

        // check with empty input list
        List<Positionable> emptyInput = new ArrayList();
        List<Positionable> result = tucoRule.filter(emptyInput);
        List<Positionable> targets = new ArrayList<>();
        for (Positionable pos : result) {
            if (Positionable.Level.TOP.equals(pos.getLevel()) && tuco.getCar() == pos.getCar()) {
                targets.add(pos);
            }
        }

        Assert.assertNotNull("Targets for tuco not null", result);
        Assert.assertEquals("Result has list size 1.", 1, result.size());
        Assert.assertEquals("1 players as target for tuco.", 1, targets.size());

        // check with non empty input list
        List<Positionable> alreadyFilledInput = new ArrayList();
        alreadyFilledInput.add(p0b);
        List<Positionable> result2 = tucoRule.filter(alreadyFilledInput);
        List<Positionable> targets2 = new ArrayList<>();
        for (Positionable pos : result2) {
            if (Positionable.Level.TOP.equals(pos.getLevel()) && tuco.getCar() == pos.getCar()) {
                targets2.add(pos);
            }
        }

        Assert.assertNotNull("Targets for tuco not null", result2);
        Assert.assertEquals("Result list has size 2", 2, result2.size());
        Assert.assertEquals("1 players as target for tuco.", 1, targets2.size());
    }
}
