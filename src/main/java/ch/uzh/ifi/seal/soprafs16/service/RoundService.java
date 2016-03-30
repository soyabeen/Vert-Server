package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.Turn;
import ch.uzh.ifi.seal.soprafs16.exception.InvalidInputException;
import ch.uzh.ifi.seal.soprafs16.model.Card;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Round;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.RoundRepository;
import org.hibernate.cfg.NotYetImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
     * Check the input arguments gameId and nthRound. Both must be: <br/>
     * - not null
     * - not 0
     * - positive numbers
     *
     * @param gameId
     * @param nthRound
     * @throws InvalidInputException
     */
    private void checkInputArgsGameIdAndNthRound(Long gameId, Integer nthRound) {

        if (gameId == null || gameId <= 0) {
            throw new InvalidInputException("Invalid arg. gameId <" + gameId + ">, must be a positive number.");
        }
        if (nthRound == null || nthRound <= 0) {
            throw new InvalidInputException("Invalid arg. ntRound <" + nthRound + ">, must be a positive number.");
        }

    }

    /**
     * Retrieves a round chosen by its belonging to a game and its position.
     *
     * @param gameId
     * @param nthRound
     * @return The round object.
     */
    public Round getRoundById(Long gameId, Integer nthRound) {
        logger.debug("getRoundById with gameId: {} nthRound: {}", gameId, nthRound);

        // throws InvalidInputException if not valid
        checkInputArgsGameIdAndNthRound(gameId, nthRound);

        Game game = gameRepo.findOne(gameId);
        logger.debug(game.toString());
        if (game == null || game.getId() == null) {
            throw new InvalidInputException("Invalid arg. gameId  <" + gameId + ">, could not find a matching game.");
        }

        Round round = roundRepo.findByGameAndNthRound(game, nthRound);
        return round;
    }

    /**
     * Retrieves a list of Turns belonging to a game and a chosen round.
     *
     * @param gameId
     * @param nthRound
     * @return
     */
    public List<Turn> listTurnsForRound(Long gameId, Integer nthRound) {
        logger.debug("listTurnsForRound with gameId: {} nthRound: {}", gameId, nthRound);
        Round round = getRoundById(gameId, nthRound);
        return round.getTurns();
    }

    /**
     * Plays chosen card.
     *
     * @param userToken
     * @param playedCard
     * @return
     */
    public String playACard(String userToken, Card playedCard) {
        throw new NotYetImplementedException();
    }
}
