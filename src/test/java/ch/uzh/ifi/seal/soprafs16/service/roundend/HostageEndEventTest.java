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
public class HostageEndEventTest {

    @Test
    public void everyPlayerOnLocomotiveGetsRansom() {

        Game game = new Game();
        game.setNrOfCars(3);

        Player p0b = PositionedPlayer.builder()
                .id(1L)
                .withUserName("loc-0-bot")
                .onLowerLevelAt(0).build();
        game.addPlayer(p0b);

        Player p0b2 = PositionedPlayer.builder()
                .id(2L)
                .withUserName("loc-0-bot-2")
                .onLowerLevelAt(0).build();
        game.addPlayer(p0b2);

        Player p0t = PositionedPlayer.builder()
                .id(3L)
                .withUserName("loc-0-top")
                .onUpperLevelAt(0).build();
        game.addPlayer(p0t);

        Player p1t = PositionedPlayer.builder()
                .id(4L)
                .withUserName("car-1-top")
                .onUpperLevelAt(1).build();
        game.addPlayer(p1t);

        Player p1b = PositionedPlayer.builder()
                .id(5L)
                .withUserName("car-1-bot")
                .onLowerLevelAt(1).build();
        game.addPlayer(p1b);

        Player p2b = PositionedPlayer.builder()
                .id(6L)
                .withUserName("car-2-bot")
                .onLowerLevelAt(2).build();
        game.addPlayer(p2b);


        Hostage hostage = new Hostage();
        List<Positionable> result = hostage.execute(game);
        List<Player> playersWithRansom = new ArrayList<>();
        for (Positionable pos : result) {
            if (pos instanceof Player) {
                Player p = (Player) pos;
                if (!p.getLoots().isEmpty()) {
                    if (p.getLoots().get(0).getOwnerId() == p.getId()) {
                        playersWithRansom.add(p);
                    }
                }
            }
        }

        Assert.assertNotNull("Players with ransom not null", playersWithRansom);
        Assert.assertEquals("3 players with ransom.", 3, playersWithRansom.size());
    }
}
