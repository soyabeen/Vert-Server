package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.dto.TurnDTO;
import ch.uzh.ifi.seal.soprafs16.model.Card;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Round;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.RoundRepository;
import ch.uzh.ifi.seal.soprafs16.utils.InputArgValidator;
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
        nextPlayerId = getNextPlayerId(gameId, nthround);
        return nextPlayerId;
    }

    public void setNextPlayer(Long gameId, Integer nthround, Long currPlayerId) {
        Long nextPlayerId = -1L;

        InputArgValidator.checkInputArgsGameIdAndNthRound(gameId, nthround);
        InputArgValidator.checkAvailabeId(currPlayerId, playerRepo, "setNextPlayer() in PhaseLogicService has wrong " +
                "PlayerId");


        Game game = gameRepo.findOne(gameId);
        List<Player> players = game.getPlayers();
        currentPlayer = playerRepo.findOne(currPlayerId);

        if( (players.indexOf( currentPlayer ) + 1) == players.size()) {
            //at end of List, next Player will be at Index 0
            nextPlayerId = players.get(0).getId();
        } else {
            //if currentPlayer not at the end of the list (1 because of indices starting at 0)
            nextPlayerId = players.get( players.indexOf( currentPlayer ) + 1).getId();
        }

        game.setNextPlayerId(nextPlayerId);
        gameRepo.save(game);

        /*
        //reexamine what happens when nextPlayer is startPlayer again. ☠ debug?
        if(round.getStartPlayerId() == foundPlayerId) {
            // TODO: execute Action Phase
            setStartPlayer(game, nthround, foundPlayerId);
        }
        */
    }

    public void setCurrentPlayer(Long gameId, Integer nthround, Long playerId) {
        InputArgValidator.checkInputArgsGameIdAndNthRound(gameId, nthround);
        InputArgValidator.checkAvailabeId(playerId, playerRepo, "Given playerId is no valid player for method " +
                "\'setCurrentPlayer()\' in PhaseLogicService.java");

        Game game = gameRepo.findOne(gameId);
        game.setCurrentPlayerId(playerId);
        gameRepo.save(game);
        //setNextPlayer(gameId, nthround, playerId);
    }

    /**
     * Returns the ID of the next player in collection based on the current player.
     * @param gameId
     * @param nthround
     * @return player ID of next player
     */
    protected Long getNextPlayerId(Long gameId, Integer nthround) {
        Round round = roundRepo.findByGameIdAndNthRound(gameId, nthround);
        Long startPlayerId = round.getStartPlayerId();
        Game game = gameRepo.findOne(gameId);
        currentPlayer = playerRepo.findOne( game.getCurrentPlayerId() );
        List<Player> playerList = game.getPlayers();
        Integer nextPlayerIndex = (playerList.indexOf(currentPlayer) + 1) % playerList.size();
        Long nextPlayerId = playerList.get( nextPlayerIndex ).getId();
        Player nextPlayer = playerRepo.findOne( nextPlayerId );

        if(startPlayerId == nextPlayerId) {
            // nextPlayerIndex muss um 1 inkrementiert werden (aber Out of Bounds Index beachten!)
            startPlayerId = playerList.get( (playerList.indexOf(nextPlayer) + 1) % playerList.size() ).getId();
            setStartPlayer(game, nthround, startPlayerId);
            return startPlayerId;

        } else if( (playerList.indexOf(currentPlayer) + 1) == playerList.size()) {
            //at end of List, next Player will be at Index 0
            return playerList.get(0).getId();
        } else {
            //if currentPlayer not at the end of the list (1 because of indices starting at 0)
            return playerList.get( playerList.indexOf(currentPlayer) + 1).getId();
        }
    }

    protected Long getStartPlayerId(Long gameId, Integer nthround) {
        InputArgValidator.checkInputArgsGameIdAndNthRound(gameId, nthround);

        Game game = gameRepo.findOne(gameId);
        Round round = roundRepo.findByGameIdAndNthRound(game.getId(), nthround);

        return round.getStartPlayerId();
    }

    public void setStartPlayer(Game game, Integer nthround, Long playerId) {
        InputArgValidator.checkInputArgsGameIdAndNthRound(game.getId(), nthround);
        InputArgValidator.checkAvailabeId(playerId, playerRepo, "No valid player found for \'setStartPlayer\' in " +
                "PhaseLogicService");
        Round round = roundRepo.findByGameIdAndNthRound(game.getId(), nthround);

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
        Round round = roundRepo.findByGameIdAndNthRound(game.getId(), nthround);

        cardStack = (LinkedList<Card>) round.getCardStack();
        //set owner of top card to current player
        setCurrentPlayer(game.getId(), nthround, cardStack.peekFirst().getOwner());
    }

    /**
     * Start computing possibilities for Player.
     * @param gameId
     * @param nthround
     */
    public void receivePossibilities(Long gameId, Integer nthround) {
        InputArgValidator.checkInputArgsGameIdAndNthRound(gameId, nthround);
        Game game = gameRepo.findOne(gameId);
        Round round = roundRepo.findByGameIdAndNthRound(game.getId(), nthround);

        // retrieve played card
        cardStack = (LinkedList<Card>) round.getCardStack();
        Card top = cardStack.peek();

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
