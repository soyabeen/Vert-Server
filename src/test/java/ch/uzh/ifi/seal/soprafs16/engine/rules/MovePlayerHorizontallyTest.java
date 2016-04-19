package ch.uzh.ifi.seal.soprafs16.engine.rules;

import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by soyabeen on 19.04.16.
 */
public class MovePlayerHorizontallyTest {

    @Test
    public void evalTrueWhenPositionedOnBottom() {
        Player playerOnBottom = new Player();
        playerOnBottom.setLevel(Positionable.Level.BOTTOM);
        playerOnBottom.setCar(0);

        MovePlayerHorizontally rule = new MovePlayerHorizontally(playerOnBottom, 3, 1);

        Assert.assertTrue("Expect true for player on bottom level.", rule.evaluate());
    }

    @Test
    public void evalTrueWhenPositionedOnTop() {
        Player playerOnTop = new Player();
        playerOnTop.setLevel(Positionable.Level.TOP);
        playerOnTop.setCar(0);

        MovePlayerHorizontally rule = new MovePlayerHorizontally(playerOnTop, 3, 1);

        Assert.assertTrue("Expect true for player on top level.", rule.evaluate());
    }

    @Test
    public void evalFalseWhenPositionedOnUnknownLevel() {
        Player playerOnUnknown = new Player();
        playerOnUnknown.setLevel(null);
        playerOnUnknown.setCar(0);

        MovePlayerHorizontally rule = new MovePlayerHorizontally(playerOnUnknown, 3, 1);

        Assert.assertFalse("Expect false for player on unknown level.", rule.evaluate());
    }

    @Test
    public void evalTrueWhenPositionedOnTrain() {
        int trainLength = 3;

        Player playerOnTrain = new Player();
        playerOnTrain.setLevel(Positionable.Level.TOP);
        playerOnTrain.setCar(1);

        MovePlayerHorizontally rule = new MovePlayerHorizontally(playerOnTrain, trainLength, 1);
        Assert.assertTrue("Expect true for player on train.", rule.evaluate());
    }

    @Test
    public void evalTrueWhenPositionedAfterTrain() {
        int trainLength = 3;
        int posAfterTrain = 4;

        Player playerOnTrain = new Player();
        playerOnTrain.setLevel(Positionable.Level.TOP);
        playerOnTrain.setCar(posAfterTrain);

        MovePlayerHorizontally rule = new MovePlayerHorizontally(playerOnTrain, trainLength, 1);
        Assert.assertFalse("Expect false for player not on train.", rule.evaluate());
    }

    @Test
    public void evalTrueWhenPositionedBeforeTrain() {
        int trainLength = 3;
        int posBeforeTrain = -1;

        Player playerOnTrain = new Player();
        playerOnTrain.setLevel(Positionable.Level.TOP);
        playerOnTrain.setCar(posBeforeTrain);

        MovePlayerHorizontally rule = new MovePlayerHorizontally(playerOnTrain, trainLength, 1);
        Assert.assertFalse("Expect false for player not on train.", rule.evaluate());
    }

    @Test
    public void evalTrueWhenPositionedAtEdgeOfTrain() {
        int trainLength = 3;

        Player playerOnLoc = new Player();
        playerOnLoc.setLevel(Positionable.Level.TOP);
        playerOnLoc.setCar(0);
        MovePlayerHorizontally ruleLoc = new MovePlayerHorizontally(playerOnLoc, trainLength, 1);
        Assert.assertTrue("Expect true for player on locomotive.", ruleLoc.evaluate());

        Player playerOnLast = new Player();
        playerOnLast.setLevel(Positionable.Level.TOP);
        playerOnLast.setCar(trainLength);
        MovePlayerHorizontally ruleLast = new MovePlayerHorizontally(playerOnLast, trainLength, 1);
        Assert.assertTrue("Expect true for player on last car.", ruleLast.evaluate());
    }

}
