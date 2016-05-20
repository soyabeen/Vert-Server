package ch.uzh.ifi.seal.soprafs16.utils;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Round;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by soyabeen on 20.05.16.
 */
public class FastLaneRoundConfigurator extends RoundConfigurator {

    @Override
    public LinkedList<Round> generateRoundsForGame(Game game) {
        List<RoundConfiguration> stationConfigs = getStationConfigurations();
        Collections.shuffle(stationConfigs);

        LinkedList<Round> rounds = new LinkedList<>();
        rounds.add(buildRoundWithConfig(game, 1, stationConfigs.get(0)));
        return rounds;
    }
}
