package ch.uzh.ifi.seal.soprafs16.service.roundend;

import ch.uzh.ifi.seal.soprafs16.helper.PositionedPlayer;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soyabeen on 18.05.16.
 */
public class PivotableEndEventTest {
    @Test
    public void allPlayersOnTopMoveToLastCar() {

        int numberOfTrainCars = 3;

        Game game = new Game();
        game.setNrOfCars(numberOfTrainCars);

        Player p0b = PositionedPlayer.builder()
                .withUserName("loc-0-bot")
                .onLowerLevelAt(0).build();
        game.addPlayer(p0b);

        Player p0b2 = PositionedPlayer.builder()
                .withUserName("loc-0-bot-2")
                .onLowerLevelAt(0).build();
        game.addPlayer(p0b2);

        Player p0t = PositionedPlayer.builder()
                .withUserName("loc-0-top")
                .onUpperLevelAt(0).build();
        game.addPlayer(p0t);

        Player p1t = PositionedPlayer.builder()
                .withUserName("car-1-top")
                .onUpperLevelAt(1).build();
        game.addPlayer(p1t);

        Player p1b = PositionedPlayer.builder()
                .withUserName("car-1-bot")
                .onLowerLevelAt(1).build();
        game.addPlayer(p1b);

        Player p2b = PositionedPlayer.builder()
                .withUserName("car-2-bot")
                .onLowerLevelAt(2).build();
        game.addPlayer(p2b);

        Player p2t = PositionedPlayer.builder()
                .withUserName("car-2-top")
                .onUpperLevelAt(2).build();
        game.addPlayer(p2t);


        PivotablePole pivo = new PivotablePole();
        List<Positionable> result = pivo.execute(game);
        List<Positionable> playersOnTopLast = new ArrayList<>();
        for (Positionable pos : result) {
            if (Positionable.Level.TOP.equals(pos.getLevel()) && numberOfTrainCars - 1 == pos.getCar()) {
                playersOnTopLast.add(pos);
            }
        }

        Assert.assertNotNull("Players on top last car not null", playersOnTopLast);
        Assert.assertEquals("3 players should be on top last car.", 3, playersOnTopLast.size());
    }
}
