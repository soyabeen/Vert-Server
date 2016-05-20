package ch.uzh.ifi.seal.soprafs16.service.roundend;

import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.constant.LootType;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Loot;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;

/**
 * Created by devuser on 18.05.2016.
 */
public class MarshalsRevengeTest {

    private static final Logger logger = LoggerFactory.getLogger(RebellionTest.class);

    Game game;
    Player player1, player2, player3;
    Loot l1, l1_2, l2;

    @Before
    public void setUp() throws Exception {
        game = new Game();
        game.setId(1L);
        game.setNrOfCars(4);
        game.setPositionMarshal(2);

        player1 = new Player();
        player1.setId(1L);
        player1.setUsername("P1");
        player1.setCar(2);
        player1.setLevel(Positionable.Level.TOP);
        player1.setCharacter(Character.BELLE);
        l1 = new Loot(LootType.PURSE_SMALL, game.getId(), 250);
        l1.setOwnerId(1L);
        l1.setId(1L);
        l1_2 = new Loot(LootType.PURSE_BIG, game.getId(), 350);
        l1_2.setOwnerId(1L);
        l1_2.setId(2L);
        player1.addLoot(l1);
        player1.addLoot(l1_2);
        game.addPlayer(player1);

        player2 = new Player();
        player2.setId(2L);
        player2.setUsername("P2");
        player2.setCar(2);
        player2.setLevel(Positionable.Level.TOP);
        player2.setCharacter(Character.DJANGO);
        l2 = new Loot(LootType.PURSE_BIG, game.getId(), 500);
        l2.setId(3L);
        l2.setOwnerId(2L);
        player2.addLoot(l2);
        player2.addLoot(new Loot(LootType.JEWEL, game.getId(), 500));
        game.addPlayer(player2);

        player3 = new Player();
        player3.setId(3L);
        player3.setUsername("P3");
        player3.setCar(2);
        player3.setLevel(Positionable.Level.TOP);
        player3.setCharacter(Character.DJANGO);
        player3.addLoot(new Loot(LootType.STRONGBOX, game.getId(), 500));
        player3.addLoot(new Loot(LootType.JEWEL, game.getId(), 500));
        game.addPlayer(player3);
    }

    @Test
    public void testPlayersOnTopOfMarshalLosePurse() {



        MarshalsRevenge event = new MarshalsRevenge();
        List<Positionable> result = event.execute(game);
        List<Loot> loot = new ArrayList<>();

        for(Positionable p : result) {
            Assert.assertThat("Check Type of Elements in result", p, anyOf(instanceOf(Loot.class), instanceOf(Player.class)));
            if( p instanceof Loot) {
                Assert.assertNull( ((Loot) p).getOwnerId() );
                loot.add((Loot) p);
            }
        }

        Assert.assertThat(loot.get(0).getValue(), is(250));
        Assert.assertThat(loot.get(1).getValue(), is(500));
        Assert.assertThat(result.size(), is(4));

        // Player 1 Test
        // Player 1 has less loot than in setup
        Assert.assertThat(player1.getLoots().size(), is(1));
        // Player 1 lost a purse
        Assert.assertEquals(player1.getLoots().get(0).getType(), LootType.PURSE_BIG);
        // Player 1 lost the purse with the smallest value
        Assert.assertThat(player1.getLoots().get(0).getValue(), is(350));

        // Player 2 Test
        Assert.assertThat(player2.getLoots().size(), is(1));
        Assert.assertEquals(player2.getLoots().get(0).getType(), LootType.JEWEL);

        // Player 3 Test
        Assert.assertThat(player3.getLoots().size(), is(2));


    }




}