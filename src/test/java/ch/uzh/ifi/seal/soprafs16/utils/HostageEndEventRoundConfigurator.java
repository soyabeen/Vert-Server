package ch.uzh.ifi.seal.soprafs16.utils;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Round;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by soyabeen on 25.05.16.
 */
public class HostageEndEventRoundConfigurator extends RoundConfigurator {

    @Override
    public LinkedList<Round> generateRoundsForGame(Game game) {
        List<RoundConfiguration> stationConfigs = getStationConfigurations();

        LinkedList<Round> rounds = new LinkedList<>();
        rounds.add(buildRoundWithConfig(game, 1, stationConfigs.get(2)));
        return rounds;
    }
}
