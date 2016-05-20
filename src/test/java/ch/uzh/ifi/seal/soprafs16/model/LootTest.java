package ch.uzh.ifi.seal.soprafs16.model;

import ch.uzh.ifi.seal.soprafs16.constant.LootType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by antoio on 20.05.2016.
 */
public class LootTest {

    Loot loot1, loot2;

    @Before
    public void setUp() throws Exception {
        Game game = new Game();
        loot1 = new Loot(LootType.JEWEL, 1L, 250);
        loot1.setCar(2);
        loot1.setOwnerId(2L);
        loot1.setId(2L);
        game.setNrOfCars(3);
        game.addLoot(loot1);

        loot2 = new Loot(LootType.JEWEL, 1L, 500);
        loot2.setOwnerId(3L);
        loot2.setId(3L);
    }

    @Test
    public void moveToHead() throws Exception {
        loot1.moveToHead(1);
        Assert.assertThat(loot1.getCar(), is(1));
    }

    @Test
    public void moveToTail() throws Exception {
        loot1.moveToTail(1);
        Assert.assertThat(loot1.getCar(), is(3));
    }

    @Test
    public void equals() throws Exception {
        Assert.assertThat(loot1.equals(loot2), is(false));
        Assert.assertTrue(loot1.equals(loot1));
    }

}