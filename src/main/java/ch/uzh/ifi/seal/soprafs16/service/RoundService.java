package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.exception.InvalidInputException;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Round;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.RoundRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by soyabeen on 26.03.16.
 */
@Service("gameRoundService")
public class RoundService {

    private static final Logger logger = LoggerFactory.getLogger(RoundService.class);

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private RoundRepository roundRepo;

    public Round getRoundById(Long gameId, Long roundId) {

        Round round = roundRepo.findOne(roundId);
        if (round == null) {
            throw new InvalidInputException("GetRoundById - No round object with id " + roundId + " exists.");
        }
        if (gameId != round.getGame().getId()) {
            throw new InvalidInputException("GetRoundById - Provided gameId (" + gameId
                    + ") does not match gameId from round object (" + round.getGame().getId() + ").");
        }
        return round;
    }

}
