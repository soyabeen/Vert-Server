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
public class RoundServiceTest {

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
            }else{
                memory.add(temp_round);
            }
        }
        Assert.assertTrue("No round configuration/position(nthRound) is used multiple times.", true);
    }


}
