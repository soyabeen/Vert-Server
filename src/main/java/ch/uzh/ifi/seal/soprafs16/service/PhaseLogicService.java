package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.model.Card;
import ch.uzh.ifi.seal.soprafs16.utils.InputArgValidator;
import ch.uzh.ifi.seal.soprafs16.dto.TurnDTO;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Round;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.RoundRepository;
import jdk.internal.util.xml.impl.Input;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by antoio on 4/20/16.
 */
@Service("PhaseLogicService")
public class PhaseLogicService {

    @Autowired
    GameRepository gameRepo;

    @Autowired
    RoundRepository roundRepo;

    @Autowired
    PlayerRepository playerRepo;

    Player currentPlayer;
    LinkedList<Card> cardStack;
    TurnDTO turnDTO;

    /**
     * Returns the Player ID of the following Player. This way a client can poll more frequently if desired.
     * @param gameId
     * @return Player ID for the following Player
     */
    public Long getNextPlayer(Long gameId, Integer nthround) {
        Long nextPlayerId = -1L;

        InputArgValidator.checkInputArgsGameIdAndNthRound(gameId, nthround);

        //get all prerequisites
        Game game = gameRepo.findOne(gameId);
        nextPlayerId = getNextPlayerId(game);
        return nextPlayerId;
    }

    public void setNextPlayer(Long gameId, Integer nthround) {
        Long foundPlayerId = -1L;

        InputArgValidator.checkInputArgsGameIdAndNthRound(gameId, nthround);


        Game game = gameRepo.findOne(gameId);
        Round round = roundRepo.findByGameAndNthRound(game, nthround);

        foundPlayerId = getNextPlayerId(game);

        //reexamine what happens when nextPlayer is startPlayer again. ☠ debug?
        if(round.getStartPlayerId() == foundPlayerId) {
            // execute Action Phase
            foundPlayerId = getNextPlayerId(game);
            setStartPlayer(game, nthround, foundPlayerId);
        }

        game.setNextPlayerId(foundPlayerId);
        gameRepo.save(game);
    }

    public void setCurrentPlayer(Long gameId, Integer nthround, Long playerId) {
        InputArgValidator.checkInputArgsGameIdAndNthRound(gameId, nthround);
        InputArgValidator.checkAvailabeId(playerId, playerRepo, "Given playerId is no valid player for method " +
                "\'setCurrentPlayer()\' in PhaseLogicService.java");

        Game game = gameRepo.findOne(gameId);
        game.setCurrentPlayerId(playerId);
        gameRepo.save(game);
        setNextPlayer(gameId, nthround);
    }

    /**
     * Returns only the ID of the next player in collection. Steps through
     * @param game
     * @return player ID of next player
     */
    protected Long getNextPlayerId(Game game) {
        currentPlayer = playerRepo.findOne( game.getCurrentPlayerId() );
        List<Player> playerList = game.getPlayers();


        if( (playerList.indexOf(currentPlayer) + 1) == playerList.size()) {
            //at end of List, next Player will be at Index 0
            return playerList.get(0).getId();
        } else {
            //if currentPlayer not at the end of the list (1 because of indices starting at 0)
            return playerList.get(playerList.indexOf(currentPlayer) + 1).getId();
        }
    }

    protected Long getStartPlayerId(Long gameId, Integer nthround) {
        InputArgValidator.checkInputArgsGameIdAndNthRound(gameId, nthround);

        Game game = gameRepo.findOne(gameId);
        Round round = roundRepo.findByGameAndNthRound(game, nthround);

        return round.getStartPlayerId();
    }

    public void setStartPlayer(Game game, Integer nthround, Long playerId) {
        InputArgValidator.checkInputArgsGameIdAndNthRound(game.getId(), nthround);
        InputArgValidator.checkAvailabeId(playerId, playerRepo, "No valid player found for \'setStartPlayer\' in " +
                "PhaseLogicService");
        Round round = roundRepo.findByGameAndNthRound(game, nthround);

        round.setStartPlayerId(playerId);
        roundRepo.save(round);

        setCurrentPlayer(game.getId(), nthround, playerId);
    }

    /**
     * Method is called first when Action Phase is initiated.
     * @param game
     * @param nthround
     */
    public void peekAndSetCurrentPlayer(Game game, Integer nthround) {
        InputArgValidator.checkInputArgsGameIdAndNthRound(game.getId(), nthround);
        Round round = roundRepo.findByGameAndNthRound(game, nthround);

        cardStack = round.getCardStack();
        //set owner of top card to current player
        setCurrentPlayer(game.getId(), nthround, cardStack.peekFirst().getOwner().getId());
    }

    /**
     * Start computing possibilities for Player.
     * @param gameId
     * @param nthround
     */
    public void receivePossibilities(Long gameId, Integer nthround) {
        InputArgValidator.checkInputArgsGameIdAndNthRound(gameId, nthround);
        Game game = gameRepo.findOne(gameId);
        Round round = roundRepo.findByGameAndNthRound(game, nthround);

        // retrieve played card
        Card top = round.getCardStack().peek();

        // give played card to rule engine

        // take back result and give it to sendPossibilities()

        throw new IllegalStateException("Not yet implemented");
    }

    /**
     * Send the calculated possbilities from the rule engine to the client.
     * @return object with possible selections for the user
     */
    public TurnDTO sendPossibilities() {
        //turnDTO = new TurnDTO();
        //TODO: fill DTO object with possibilities from current turn
        //return turnDTO;
        throw  new IllegalStateException("Not yet implemented");
    }

    protected void updateGameState(Game game) {
        // update Game State with new game object
        // or update game without asking rule engine
        throw  new IllegalStateException("Not yet implemented");
    }
}
