package ch.uzh.ifi.seal.soprafs16.service.roundend;

import ch.uzh.ifi.seal.soprafs16.constant.LootType;
import ch.uzh.ifi.seal.soprafs16.helper.PositionedPlayer;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Loot;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mirkorichter on 18.05.16.
 */
public class PickpocketingTest {

    @Test
    public void testPlayersPickedUpLoot() {

        Game game = new Game();
        game.setNrOfCars(4);
        game.setPositionMarshal(2);

        Player p1 = PositionedPlayer.builder()
                .onLowerLevelAt(1).build();

        Player p2 = PositionedPlayer.builder()
                .onLowerLevelAt(0).build();

        Player p3 = PositionedPlayer.builder()
                .onUpperLevelAt(2).build();

        game.addPlayer(p1);
        game.addPlayer(p2);
        game.addPlayer(p3);

        Loot l1 = new Loot(LootType.PURSE_BIG, 1L, 500, 1, Positionable.Level.BOTTOM);
        Loot l2 = new Loot(LootType.JEWEL, 1L, 500, 1, Positionable.Level.BOTTOM);

        game.addLoot(l1);
        game.addLoot(l2);

        Assert.assertEquals(0,p1.getLoots().size());
        Assert.assertEquals(0,p2.getLoots().size());
        Assert.assertEquals(0,p3.getLoots().size());

        Pickpocketing event = new Pickpocketing();
        List<Positionable> result = event.execute(game);

        Assert.assertEquals(2, result.size());

        List<Loot> loots = new ArrayList<>();
        List<Player> players = new ArrayList<>();

        for (Positionable pos : result) {
            if (pos instanceof Player) {
                players.add((Player) pos);
            } else if (pos instanceof Loot) {
                loots.add((Loot) pos);
            }
        }

        Assert.assertEquals(LootType.PURSE_BIG, players.get(0).getLoots().get(0).getType());

    }

}
