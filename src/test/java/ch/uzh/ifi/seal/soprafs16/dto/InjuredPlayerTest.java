package ch.uzh.ifi.seal.soprafs16.dto;

import ch.uzh.ifi.seal.soprafs16.model.Player;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by soyabeen on 17.05.16.
 */
public class InjuredPlayerTest {

    @Test
    public void newPlayerIsNotInjured() {
        Player p = new Player("healthy-player");
        Assert.assertEquals("Healthy player has no injuries.", 0, p.getInjuries());
    }

    @Test
    public void shotPlayerIsInjured() {
        Player p = new Player("shot-player");
        p.getsShot();
        p.getsShot();
        Assert.assertEquals("Shot player has injuries.", 2, p.getInjuries());
    }
}
