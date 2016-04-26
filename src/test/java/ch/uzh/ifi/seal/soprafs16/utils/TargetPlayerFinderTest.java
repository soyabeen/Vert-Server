package ch.uzh.ifi.seal.soprafs16.utils;

import ch.uzh.ifi.seal.soprafs16.helper.PositionedPlayer;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;

/**
 * Created by soyabeen on 22.04.16.
 */
public class TargetPlayerFinderTest {

    @Test
    public void findsOnlyPlayerOnNeighboringFloor() {
        List<Player> positionedPlayers = new ArrayList<>();
        positionedPlayers.add(PositionedPlayer.builder().
                withUserName("Player0").onLowerLevelAt(0).build());
        positionedPlayers.add(PositionedPlayer.builder().
                withUserName("Player0-top").onUpperLevelAt(0).build());
        positionedPlayers.add(PositionedPlayer.builder().
                withUserName("Player1").onLowerLevelAt(1).build());
        positionedPlayers.add(PositionedPlayer.builder().
                withUserName("Player2a").onLowerLevelAt(2).build());
        positionedPlayers.add(PositionedPlayer.builder().
                withUserName("Player2b").onLowerLevelAt(2).build());
        positionedPlayers.add(PositionedPlayer.builder().
                withUserName("Player3").onLowerLevelAt(3).build());

        Player actor = PositionedPlayer.builder().withUserName("Actor").onLowerLevelAt(1).build();

        TargetFinder tf = new TargetFinder();
        List<Player> result = tf.findTargetToShootOnLowerLevel(actor, positionedPlayers);
        Assert.assertThat(result.size(), is(3));
    }

    @Test
    public void findsOnlyPlayerInSight() {
        List<Player> positionedPlayers = new ArrayList<>();
        positionedPlayers.add(PositionedPlayer.builder().
                withUserName("Player0a").onUpperLevelAt(0).build());
        positionedPlayers.add(PositionedPlayer.builder().
                withUserName("Player0b").onUpperLevelAt(0).build());
        positionedPlayers.add(PositionedPlayer.builder().
                withUserName("Player1-bottom").onLowerLevelAt(1).build());
        positionedPlayers.add(PositionedPlayer.builder().
                withUserName("Player2").onUpperLevelAt(2).build());
        positionedPlayers.add(PositionedPlayer.builder().
                withUserName("Player3a").onUpperLevelAt(3).build());
        positionedPlayers.add(PositionedPlayer.builder().
                withUserName("Player3b").onUpperLevelAt(3).build());
        positionedPlayers.add(PositionedPlayer.builder().
                withUserName("Player3-bottom").onLowerLevelAt(3).build());

        Player actor = PositionedPlayer.builder().withUserName("Actor").onUpperLevelAt(1).build();

        TargetFinder tf = new TargetFinder();
        List<Player> result = tf.findTargetToShootOnUpperLevel(actor, positionedPlayers);
        Assert.assertThat(result.size(), is(3));
    }
}
