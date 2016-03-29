package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Round;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by soyabeen on 26.03.16.
 */
@Service("gameRoundService")
public class RoundService {

    private static final Logger logger = LoggerFactory.getLogger(RoundService.class);

    public Round getRoundById(Long gameId, Long roundId) {

        return null;
    }

    public Round getRoundById(Game game, Long roundId) {

        return null;
    }

}
