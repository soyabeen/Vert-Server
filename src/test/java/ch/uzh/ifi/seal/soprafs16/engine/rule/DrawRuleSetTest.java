package ch.uzh.ifi.seal.soprafs16.engine.rule;

import org.junit.Assert;
import org.junit.Test;

/**
 * This test is total nonsense.
 * <p>
 * Created by soyabeen on 19.05.16.
 */
public class DrawRuleSetTest {

    @Test
    public void testForEmptyArray() {
        DrawRuleSet draw = new DrawRuleSet();
        Assert.assertTrue("Simulated possibilities are empty.", draw.simulate(null, null).isEmpty());
        Assert.assertTrue("Executed actions are empty.", draw.execute(null).isEmpty());
    }
}
