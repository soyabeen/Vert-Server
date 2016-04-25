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
     * Retrieves a list of turns belonging to a game and a chosen round.
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
     * Executes chosen action of Player.
     *
     * @param gameId Identifier of game
     * @param move Action chosen by player
     * @return turnId Nth-turn of player
     */
    public String makeAMove(Long gameId, Integer nthRound, Move move) {
        // holds playerId to lookup player in repo
        Long tmpId;

        // throws InvalidInputException if not valid
        checkInputArgsGameIdAndNthRound(gameId, nthRound);

        Game game = gameRepo.findOne(gameId);
        move.setGame(game);
        Round round = roundRepo.findByGameAndNthRound(game, nthRound);

        Player currentPlayer;
        if( move != null  ) {
            // get owner of card (= player) to access players hand
            tmpId = move.getPlayer().getId();
            currentPlayer = playerRepo.findOne(tmpId);

        } else throw new InvalidInputException("Can't play move, that is empty/null.");

        if( !move.isPass() ) {
            // Player played a card

            // add played card
            playACard(round, move.getPlayedCard());

        } else {
            // Player passed
            passAndTake3(currentPlayer);

        }

        // save Move
        moveRepo.save(move);

        // return turnId for player (where turnId is nth-move of player)
        return String.valueOf(currentPlayer.getTotalMadeMoves());
    }

    /**
     * Saves played card in card stack.
     * @param playedCard
     * @param round
     * @return round
     */
    protected Round playACard(Round round, Card playedCard) {
        // add played card to card stack
        round.addNewlyPlayedCard(playedCard);

        playedCard.getOwner().incrementTotalMadeMoves();

        // remove Card from player hand
        removeCardFromHand(playedCard.getOwner(), playedCard);

        return roundRepo.save(round);
    }

    protected void removeCardFromHand(Player currentPlayer, Card playedCard) {
        List<Card> playerHand = currentPlayer.getHand();

        // go through hand and remove same card type
        playerHand.remove(playedCard);

        // save new hand
        currentPlayer.setHand(playerHand);
        playerRepo.save(currentPlayer);
    }

    /**
     * Passes the turn and adds 3 cards into players hand
     * @param currentPlayer
     */
    protected void passAndTake3(Player currentPlayer) {
        currentPlayer.take3Cards();
        currentPlayer.incrementTotalMadeMoves();
        playerRepo.save(currentPlayer);
    }


}
