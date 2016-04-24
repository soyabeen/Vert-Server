package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.dto.TurnDTO;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Round;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.RoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    Player startPlayer;
    String startPlayerToken;
    TurnDTO turnDTO;

    /**
     * Returns the Player ID of the following Player. This way a client can poll more frequently if desired.
     * @param gameId
     * @return Player ID for the following Player
     */
    public Long getNextPlayer(Long gameId, Integer nthround) {
        Long foundPlayerId = -1L;

        // TODO: add error checking

        //get all prerequisites
        Game game = gameRepo.findOne(gameId);
        foundPlayerId = getNextPlayerId(game);
        return foundPlayerId;
    }

    public void setNextPlayer(Long gameId, Integer nthround) {
        //TODO: add error checking
        Long foundPlayerId = -1L;
        Game game = gameRepo.findOne(gameId);
        Round round = roundRepo.findByGameAndNthRound(game, nthround);

        foundPlayerId = getNextPlayerId(game);

        //reexamine what happens when nextPlayer is startPlayer again. debug?
        if(round.getStartPlayerId() == foundPlayerId) {
            foundPlayerId = getNextPlayerId(game);
            setStartPlayer(game, nthround, foundPlayerId);
        }

        game.setNextPlayerId(foundPlayerId);
        gameRepo.save(game);
    }

    public void setCurrentPlayer(Long gameId, Integer nthround, Long playerId) {
        Game game = gameRepo.findOne(gameId);
        game.setCurrentPlayerId(playerId);
        gameRepo.save(game);
        setNextPlayer(gameId, nthround);
    }

    //should return array index
    private Long getNextPlayerId(Game game) {
        //TODO: add error checking
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

    private Long getStartPlayerId(Long gameId, Integer nthround) {
        //TODO: add error checking
        Game game = gameRepo.findOne(gameId);
        Round round = roundRepo.findByGameAndNthRound(game, nthround);

        return round.getStartPlayerId();
    }

    public void setStartPlayer(Game game, Integer nthround, Long playerId) {
        //TODO: add error checking
        Round round = roundRepo.findByGameAndNthRound(game, nthround);

        round.setStartPlayerId(playerId);
        roundRepo.save(round);

        setCurrentPlayer(game.getId(), nthround, playerId);
    }

    public TurnDTO getPossibilities() {
        //turnDTO = new TurnDTO();
        //TODO: fill DTO object with possibilities from current turn
        //return turnDTO;
        throw  new IllegalStateException("Not yet implemented");
    }

    public void setPlayerDecision() {
        throw  new IllegalStateException("Not yet implemented");
    }

    public void startGame() {
        throw  new IllegalStateException("Not yet implemented");
    }

    public void gameOver() {
        throw  new IllegalStateException("Not yet implemented");
    }
}
