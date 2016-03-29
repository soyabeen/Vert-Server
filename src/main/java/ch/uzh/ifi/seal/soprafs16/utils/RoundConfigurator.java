package ch.uzh.ifi.seal.soprafs16.utils;

import ch.uzh.ifi.seal.soprafs16.constant.RoundEndEvent;
import ch.uzh.ifi.seal.soprafs16.constant.Turn;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Round;

import javax.naming.ConfigurationException;
import java.util.*;

/**
 * Created by soyabeen on 29.03.16.
 */
public class RoundConfigurator {

    private static final int MAX_ROUNDS_FOR_GAME = 4;

    private List<RoundConfiguration> roundConfigs;
    private List<RoundConfiguration> stationConfigs;

    public RoundConfigurator() {
        initConfigurations();
    }

    /**
     * Define the round configurations for the 2-4 player round cards and the train station cards.
     */
    private void initConfigurations() {
        roundConfigs = new ArrayList<>();
        roundConfigs.add(new RoundConfiguration(1, new Turn[]{Turn.NORMAL, Turn.DOUBLE_TURNS, Turn.NORMAL}, RoundEndEvent.NONE));
        roundConfigs.add(new RoundConfiguration(2, new Turn[]{Turn.NORMAL, Turn.HIDDEN, Turn.NORMAL, Turn.NORMAL}, RoundEndEvent.PIVOTTABLE_POLE));
        roundConfigs.add(new RoundConfiguration(3, new Turn[]{Turn.NORMAL, Turn.HIDDEN, Turn.NORMAL, Turn.HIDDEN, Turn.NORMAL}, RoundEndEvent.NONE));
        roundConfigs.add(new RoundConfiguration(4, new Turn[]{Turn.NORMAL, Turn.NORMAL, Turn.HIDDEN, Turn.REVERSE}, RoundEndEvent.ANGRY_MARSHAL));
        roundConfigs.add(new RoundConfiguration(5, new Turn[]{Turn.NORMAL, Turn.NORMAL, Turn.NORMAL, Turn.NORMAL}, RoundEndEvent.BRAKING));

        stationConfigs = new ArrayList<>();
        stationConfigs.add(new RoundConfiguration(1, new Turn[]{Turn.NORMAL, Turn.NORMAL, Turn.HIDDEN, Turn.NORMAL}, RoundEndEvent.MARSHALS_REVENGE));
        stationConfigs.add(new RoundConfiguration(2, new Turn[]{Turn.NORMAL, Turn.NORMAL, Turn.HIDDEN, Turn.NORMAL}, RoundEndEvent.PICKPOCKETING));
        stationConfigs.add(new RoundConfiguration(3, new Turn[]{Turn.NORMAL, Turn.NORMAL, Turn.HIDDEN, Turn.NORMAL}, RoundEndEvent.HOSTAGE));
    }

    protected Round buildRoundWithConfig(Game game, RoundConfiguration config) {
        return new Round(game, Arrays.asList(config.turns), config.event);
    }

    /**
     * Generate number (<code>MAX_ROUNDS_FOR_GAME</code> cards + one station card) of rounds for a game, using the
     * pre configured round definitions -> <code>ch.uzh.ifi.seal.soprafs16.utils.RoundConfigurator.RoundConfiguration</code>.
     *
     * @param game The game for the generated rounds.
     * @return List of generated round objects.
     */
    public LinkedList<Round> generateRoundsForGame(Game game) {
        LinkedList<Round> rounds = new LinkedList<>();

        if (roundConfigs == null || MAX_ROUNDS_FOR_GAME > roundConfigs.size()) {
            throw new IllegalStateException("More rounds in MAX_ROUNDS_FOR_GAME than actual round configurations.");
        }

        if (stationConfigs == null || stationConfigs.size() == 0) {
            throw new IllegalStateException("No configurations for train station cards defined.");
        }

        Collections.shuffle(roundConfigs);
        Collections.shuffle(stationConfigs);
        for (int i = 0; i < MAX_ROUNDS_FOR_GAME; i++) {
            rounds.add(buildRoundWithConfig(game, roundConfigs.get(i)));
        }
        rounds.add(buildRoundWithConfig(game, stationConfigs.get(0)));

        return rounds;
    }

    protected class RoundConfiguration {

        private int rcId;
        private Turn[] turns;
        private RoundEndEvent event;

        public RoundConfiguration(int rcId, Turn[] turns, RoundEndEvent event) {
            this.rcId = rcId;
            this.turns = turns;
            this.event = event;
        }

        public int getRcId() {
            return rcId;
        }

        public Turn[] getTurns() {
            return turns;
        }

        public RoundEndEvent getEvent() {
            return event;
        }
    }

}
