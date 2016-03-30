package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.Turn;
import ch.uzh.ifi.seal.soprafs16.exception.InvalidInputException;
import ch.uzh.ifi.seal.soprafs16.model.Card;
import ch.uzh.ifi.seal.soprafs16.model.Round;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.RoundRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * Retrieves a game round chosen by its ID.
     * @param gameId
     * @param roundId
     * @return
     */
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

    /**
     * Retrieves a list of Turns belonging to a chosen round.
     * @param gameId
     * @param roundId
     * @return
     */
    public List<Turn> listOfTurns(Long gameId, Long roundId) {
        logger.debug("gameID: " + gameId);
        logger.debug("roundID: " + roundId);

        // create container for all turns
        List<Turn> turns = new ArrayList<>();

        // retrieve turns from repo
        Round round = roundRepo.findOne(gameId);

        if(round != null) {
            // populate list with turns
            turns = round.getTurns();
        } else {
            logger.debug("No round found for id: " + gameId);
        }

        return turns;
    }

    /**
     * Plays chosen card.
     * @param userToken
     * @param playedCard
     * @return
     */
    public String playACard(String userToken, Card playedCard) {
        return "Not implemented yet";
    }
}
