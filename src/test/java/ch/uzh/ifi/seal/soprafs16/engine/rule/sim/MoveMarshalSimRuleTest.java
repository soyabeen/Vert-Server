package ch.uzh.ifi.seal.soprafs16.engine.rule.sim;

import ch.uzh.ifi.seal.soprafs16.model.Marshal;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.hamcrest.core.Is.is;

/**
 * Created by devuser on 11.05.2016.
 */
public class MoveMarshalSimRuleTest {

    private static final Logger logger = LoggerFactory.getLogger(MovePlayerSimRuleTest.class);

    @Test
    public void evalTrueWhenPositionedOnBottom() {
        int startCar = 0;
        int trainLength = 3;

        Marshal marshal = new Marshal(startCar);

        MoveMarshalSimRule rule = new MoveMarshalSimRule(trainLength);

        Assert.assertTrue("Expect true for newly initialized marshal.", rule.evaluate(marshal));

    }

    @Test
    public void evalTrueWhenPositionedOnTrain() {
        int startCar = 2;
        int trainLength = 3;

        Marshal marshal = new Marshal(startCar);

        MoveMarshalSimRule rule = new MoveMarshalSimRule(trainLength);

        Assert.assertTrue("Expect true for newly initialized marshal.", rule.evaluate(marshal));
    }

    @Test
    public void evalFalseWhenPositionedOutsideOfTrain() {
        int startCar = 4;
        int trainLength = 3;

        Marshal marshal = new Marshal(startCar);

        MoveMarshalSimRule rule = new MoveMarshalSimRule(trainLength);

        Assert.assertFalse("Expect false for marshal initialized outside of train.",
                rule.evaluate(marshal));
    }

    @Test
    public void evalFalseWhenPositionedOnRoof() {
        Marshal marshal = new Marshal(0);
        marshal.setLevel(Positionable.Level.TOP);

        MoveMarshalSimRule rule = new MoveMarshalSimRule(3);

        Assert.assertFalse("Expect false for marshal positioned on roof.", rule.evaluate(marshal));
    }

    @Test
    public void marshalCanMoveInOneDirection() {
        int trainLength = 3;
        int startCar = 0;

        Marshal marshal = new Marshal(startCar);

        MoveMarshalSimRule rule = new MoveMarshalSimRule(trainLength);
        // move marshal first time
        List<Positionable> result = rule.simulate(marshal);
        Assert.assertThat("Expect 1 emulated marshal", result.size(), is(1));

        for (Positionable em : result) {
            Assert.assertTrue("New pos (" + em.getCar() + ") must be bigger than org (" + startCar + ").", em.getCar() > startCar);
        }

    }
}