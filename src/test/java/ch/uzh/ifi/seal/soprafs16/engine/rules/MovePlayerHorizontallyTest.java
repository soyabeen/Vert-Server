package ch.uzh.ifi.seal.soprafs16.engine.rules;

import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;

/**
 * Created by soyabeen on 19.04.16.
 */
public class MovePlayerHorizontallyTest {

    private static final Logger logger = LoggerFactory.getLogger(MovePlayerHorizontallyTest.class);

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

    @Test
    public void emPlayerOnLocCanMoveOnlyInOneDirection() {
        int trainCars = 4;
        int distanceToMove = 3;
        int position = 0;
        Player playerOnLoc = new Player();
        playerOnLoc.setLevel(Positionable.Level.TOP);
        playerOnLoc.setCar(position);

        MovePlayerHorizontally rule = new MovePlayerHorizontally(playerOnLoc, trainCars, distanceToMove);
        List<Positionable> result = rule.emulate();
        Assert.assertThat("Expect 3 emulated players", result.size(), is(3));
        for (Positionable em : result) {
            Assert.assertTrue("New pos ("+em.getCar()+") must be bigger than org ("+position+").", em.getCar()>position);
        }
    }

    @Test
    public void emPlayerInMiddleMovesInBothDirection() {
        int trainCars = 4;
        int distanceToMove = 3;
        int position = 1;
        Player player = new Player();
        player.setLevel(Positionable.Level.TOP);
        player.setCar(position);

        MovePlayerHorizontally rule = new MovePlayerHorizontally(player, trainCars, distanceToMove);
        List<Positionable> result = rule.emulate();
        Assert.assertThat("Expect 3 emulated players", result.size(), is(3));
        List<Positionable> movedToLeft = new ArrayList<>();
        List<Positionable> movedToRight = new ArrayList<>();
        for (Positionable em : result) {
            if(em.getCar() < position) {
                movedToLeft.add(em);
            }else{
                movedToRight.add(em);
            }
        }
        Assert.assertThat("Expect 1 em player moved to left (head).", movedToLeft.size(), is(1));
        Assert.assertThat("Expect 2 em player moved to right (tail).", movedToRight.size(), is(2));
    }


}
