package ch.uzh.ifi.seal.soprafs16.service.roundend;

import ch.uzh.ifi.seal.soprafs16.constant.LootType;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Loot;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.junit.Test;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by mirkorichter on 18.05.16.
 */
public class GetItAllTest {

    private static final Logger logger = LoggerFactory.getLogger(GetItAllTest.class);


    @Test
    public void testStrongboxToMarshal() {

        Game game = new Game();
        game.setNrOfCars(4);
        game.setPositionMarshal(2);

        GetItAll event = new GetItAll();
        List<Positionable> result = event.execute(game, new ArrayList<>());

        Assert.assertEquals(1,result.size());

        Loot loot = (Loot) result.get(0);

        Assert.assertEquals(LootType.STRONGBOX, loot.getType());
        Assert.assertEquals(Positionable.Level.BOTTOM, loot.getLevel());
        Assert.assertEquals(2, loot.getCar());

    }

}
