package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.Turn;
import ch.uzh.ifi.seal.soprafs16.exception.InvalidInputException;
import ch.uzh.ifi.seal.soprafs16.model.*;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.MoveRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.RoundRepository;
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

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private MoveRepository moveRepo;

    /**
     * Retrieves a game round chosen by its ID.
     * @param gameId
     * @param nthRound
     * @return round Desired Round object.
     */
    public Round getRoundById(Long gameId, Integer nthRound) {

        Game game = gameRepo.findOne(gameId);
        Round round = roundRepo.findByGameAndRound(game, nthRound);

        if (round == null) {
            throw new InvalidInputException("GetRoundById - No round object with id " + nthRound + " exists.");
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
     * @param nthRound
     * @return Desired list of Turns.
     */
    public List<Turn> listTurnsForRound(Long gameId, Integer nthRound) {
        logger.debug("In RoundService: listTurnsForRound()");
        logger.debug("listTurnsForRound() with gameID: {} nthRound: {}", gameId, nthRound);

        if (nthRound == null || nthRound <= 0) {
            throw new InvalidInputException("listTurnsForRound - No round object with # " + nthRound + " exists.");
        }

        if (gameId == null || gameId <= 0) {
            throw new InvalidInputException("listTurnsForRound - Provided gameId (" + gameId
                    + ") does not exist.");
        }

        // retrieve game from repo
        Game game = gameRepo.findOne(gameId);

        // retrieve round from repo
        Round round = roundRepo.findByGameAndRound(game, nthRound);

        return round.getTurns();
    }

    /**
     * Plays chosen card.
     * @param gameId Identifier of game
     * @param move Action chosen by player
     * @return turnId Nth-turn of player
     */
    public String playACard(Long gameId, Integer nthRound, Move move) {
        Game game = gameRepo.findOne(gameId);

        // need a Round to add new card
        Round round = roundRepo.findByGameAndRound(game, nthRound);
        // add played card
        round.addNewlyPlayedCard(move.getPlayedCard());
        round = roundRepo.save(round);

        // remove Card from player hand
        Player currentPlayer = playerRepo.findOne(move.getPlayedCard().getOwner().getId());
        List<Card> playerHand = currentPlayer.getHand();

        // go through hand and remove same card type
        playerHand.remove(playerHand.indexOf(move.getPlayedCard())); // TODO: ☠ debug to see if java voodo works

        // save new hand
        currentPlayer.setHand(playerHand);
        currentPlayer = playerRepo.save(currentPlayer);

        // return turnId for player (where turnId is nth-move of player)
        Integer turnId = round.getTotalMadeMoves() / 4;

        // save Move
        // after move is saved to repository, moveId will get created
        moveRepo.save(move);

        return String.valueOf(turnId);
    }
}
