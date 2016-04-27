package ch.uzh.ifi.seal.soprafs16.utils;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Round;

import java.util.LinkedList;

/**
 * This is a RoundConfigurator dedicated for the demo mode.
 * This configurator will always return only one station card.
 * <p>
 * Created by soyabeen on 26.04.16.
 */
public class DemoRoundConfigurator extends RoundConfigurator {

    @Override
    public LinkedList<Round> generateRoundsForGame(Game game) {
        LinkedList<Round> rounds = new LinkedList<>();
        rounds.add(buildRoundWithConfig(game, 1, getStationConfigurations().get(0)));
        return rounds;
    }
}
