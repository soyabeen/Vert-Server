package ch.uzh.ifi.seal.soprafs16.service.roundend;

import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.constant.LootType;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Loot;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.hamcrest.core.Is.is;

/**
 * Created by devuser on 18.05.2016.
 */
public class MarshalsRevengeTest {

    private static final Logger logger = LoggerFactory.getLogger(RebellionTest.class);

    @Test
    public void testPlayersOnTopOfMarshalLosePurse() {
        Game game = new Game();
        game.setNrOfCars(4);
        game.setPositionMarshal(2);

        Player player1 = new Player();
        player1.setId(1L);
        player1.setUsername("P1");
        player1.setCar(2);
        player1.setLevel(Positionable.Level.TOP);
        player1.setCharacter(Character.BELLE);
        Loot l1 = new Loot(LootType.PURSE_SMALL, game.getId(), 250);
        l1.setOwnerId(1L);
        l1.setId(1L);
        Loot l1_2 = new Loot(LootType.PURSE_BIG, game.getId(), 350);
        l1_2.setOwnerId(1L);
        l1_2.setId(2L);
        player1.addLoot(l1);
        player1.addLoot(l1_2);
        game.addPlayer(player1);

        Player player2 = new Player();
        player2.setId(2L);
        player2.setUsername("P2");
        player2.setCar(2);
        player2.setLevel(Positionable.Level.TOP);
        player2.setCharacter(Character.DJANGO);
        Loot l2 = new Loot(LootType.PURSE_BIG, game.getId(), 500);
        l2.setId(3L);
        l2.setOwnerId(2L);
        player2.addLoot(l2);
        player2.addLoot(new Loot(LootType.JEWEL, game.getId(), 500));
        game.addPlayer(player2);

        Player player3 = new Player();
        player3.setId(3L);
        player3.setUsername("P3");
        player3.setCar(2);
        player3.setLevel(Positionable.Level.TOP);
        player3.setCharacter(Character.DJANGO);
        player3.addLoot(new Loot(LootType.STRONGBOX, game.getId(), 500));
        player3.addLoot(new Loot(LootType.JEWEL, game.getId(), 500));
        game.addPlayer(player3);


        MarshalsRevenge event = new MarshalsRevenge();
        List<Positionable> result = event.execute(game);

        for(Positionable p : result) {
            logger.debug("Loottype in result: " + ((Loot) p).getType() );
            Assert.assertEquals(p instanceof Loot, true);
            Assert.assertNull(((Loot) p).getOwnerId());
        }

        Assert.assertThat(((Loot) result.get(0)).getValue(), is(250));
        Assert.assertThat(((Loot) result.get(1)).getValue(), is(500));
        Assert.assertThat(result.size(), is(2));

        // Player 1 Test
        Assert.assertThat(player1.getLoots().size(), is(1));
        Assert.assertEquals(player1.getLoots().get(0).getType(), LootType.PURSE_BIG);
        Assert.assertThat(player1.getLoots().get(0).getValue(), is(350));

        // Player 2 Test
        Assert.assertThat(player2.getLoots().size(), is(1));
        Assert.assertEquals(player2.getLoots().get(0).getType(), LootType.JEWEL);

        // Player 3 Test
        Assert.assertThat(player3.getLoots().size(), is(2));


    }




}