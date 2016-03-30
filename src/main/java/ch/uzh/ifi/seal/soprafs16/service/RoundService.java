package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.Turn;
import ch.uzh.ifi.seal.soprafs16.exception.InvalidInputException;
import ch.uzh.ifi.seal.soprafs16.model.*;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.MoveRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
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

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private MoveRepository moveRepo;

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
        if (game == null || game.getId() == null) {
            throw new InvalidInputException("Invalid arg. gameId  <" + gameId + ">, could not find a matching game.");
        }

        return roundRepo.findByGameAndNthRound(game, nthRound);
    }

    /**
     * Retrieves a list of Turns belonging to a game and a chosen round.
     *
     * @param gameId
     * @param nthRound
     * @return Desired list of Turns.
     */
    public List<Turn> listTurnsForRound(Long gameId, Integer nthRound) {
        logger.debug("listTurnsForRound with gameId: {} nthRound: {}", gameId, nthRound);
        Round round = getRoundById(gameId, nthRound);
        return round.getTurns();
    }

    /**
     * Plays chosen card.
     *
     * @param gameId Identifier of game
     * @param move Action chosen by player
     * @return turnId Nth-turn of player
     */
    // TODO: rename method and controller to "makeAMove(...)"
    public String playACard(Long gameId, Integer nthRound, Move move) {
        Game game = gameRepo.findOne(gameId);

        // need a Round to add new card
        Round round = roundRepo.findByGameAndNthRound(game, nthRound);

        if( move.getPlayedCard() != null ) {
            // Player played a card

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
            playerRepo.save(currentPlayer);


        } else {
            // Player passed

            // call passAndTake3-method from Round object
            // add 3 new cards from player deck to player hand
        }

        // return turnId for player (where turnId is nth-move of player)
        move.setNthMove( round.getTotalMadeMoves() / 4 );

        // save Move
        // after move is saved to repository, moveId will get created
        move = moveRepo.save(move);
        return String.valueOf(move.getNthMove());
    }
}
