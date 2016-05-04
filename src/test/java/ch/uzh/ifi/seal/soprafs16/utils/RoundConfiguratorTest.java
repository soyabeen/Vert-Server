package ch.uzh.ifi.seal.soprafs16.utils;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Round;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;


/**
 * Created by soyabeen on 30.03.16.
 */
public class RoundConfiguratorTest {

    @Test
    public void getMaxRoundsPlusOneStationCards() {
        RoundConfigurator configurator = new RoundConfigurator();
        List<Round> rounds = configurator.generateRoundsForGame(new Game());
        Assert.assertThat(rounds.size(), is(RoundConfigurator.MAX_ROUNDS_FOR_GAME + 1));
    }

    @Test
    public void everyRoundConfigurationIsUsedOnlyOnce() {
        RoundConfigurator configurator = new RoundConfigurator();
        List<Round> rounds = configurator.generateRoundsForGame(new Game());
        List<Integer> memory = new ArrayList<>();
        Integer temp_round = -1;
        for (Round r : rounds) {
            temp_round = r.getNthRound();
            if (memory.contains(temp_round)) {
                Assert.fail("Round with current position(nthRound) " + temp_round + " is used already!");
            } else {
                memory.add(temp_round);
            }
        }
        Assert.assertTrue("No round configuration/position(nthRound) is used multiple times.", true);
    }

    @Test
    public void holdsImageTypeDistinguisher() {
        RoundConfigurator configurator = new RoundConfigurator();
        List<Round> rounds = configurator.generateRoundsForGame(new Game());

        // handle station card and remove it from list
        Round station = rounds.remove(rounds.size() - 1);
        Assert.assertNotNull("Station card type not null.", station.getImgType());
        Assert.assertTrue("Station card type 1, 2 or 3.", "1".equals(station.getImgType())
                || "2".equals(station.getImgType())
                || "3".equals(station.getImgType()));

        for (Round r : rounds) {
            Assert.assertFalse("Round card type not empty.", r.getImgType().isEmpty());
        }
    }

}
