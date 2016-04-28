package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.dto.TurnDTO;
import ch.uzh.ifi.seal.soprafs16.exception.InvalidInputException;
import ch.uzh.ifi.seal.soprafs16.model.Card;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Round;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.RoundRepository;
import ch.uzh.ifi.seal.soprafs16.utils.InputArgValidator;
import ch.uzh.ifi.seal.soprafs16.utils.RoundConfigurator;
import org.hibernate.cfg.NotYetImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by antoio on 4/20/16.
 */
@Service("PhaseLogicService")
public class PhaseLogicService {
    private static final Logger logger = LoggerFactory.getLogger(GameService.class);

    @Autowired
    GameRepository gameRepo;

    @Autowired
    RoundRepository roundRepo;

    @Autowired
    PlayerRepository playerRepo;

    Game game;
    Round round;
    Player currentPlayer;
    List<Player> players;
    LinkedList<Card> cardStack;
    TurnDTO turnDTO;

    // -- Player Order Logic

    /**
     * This method is only called at the initialization of a game to set the beginning player.
     * @param gameId
     * @param playerId Starting Player
     */
    public void setInitialPlayer(Long gameId, Long playerId) {
        InputArgValidator.checkAvailabeId(gameId, gameRepo, "Given gameId is no valid game for method " +
                "\'setCurrentPlayer()\' in PhaseLogicService");
        InputArgValidator.checkAvailabeId(playerId, playerRepo, "Given playerId is no valid player for method " +
                "\'setCurrentPlayer()\' in PhaseLogicService");

        // initialize needed repositories
        game = gameRepo.findOne(gameId);

        if( gameRepo.findOne(gameId).getCurrentPlayerId() == null ) {
            setCurrentPlayerId(gameId, playerId);

        } else throw new InvalidInputException("In PhaseLogic -> setInitialPlayer(): Current Player is " +
                "already set!");
    }

    /**
     * This method is the single entry point to the Business Logic.
     * @param gameId
     * @param nthround
     */
    public void advancePlayer(Long gameId, Integer nthround) {
        InputArgValidator.checkInputArgsGameIdAndNthRound(gameId, nthround);

        // initialize needed repositories
        game = gameRepo.findOne(gameId);
        round = roundRepo.findByGameIdAndNthRound(gameId, nthround);
        players = game.getPlayers();

        if( !isGameOver(nthround) ){
            // set new current player
            setCurrentPlayerId(gameId, getNextPlayer());
        }
    }

    protected void setCurrentPlayerId(Long gameId, Long playerId) {
        InputArgValidator.checkAvailabeId(gameId, gameRepo, "Given gameId is no valid game for method " +
                "\'setCurrentPlayer()\' in PhaseLogicService");
        InputArgValidator.checkAvailabeId(playerId, playerRepo, "Given playerId is no valid player for method " +
                "\'setCurrentPlayer()\' in PhaseLogicService");

        game.setCurrentPlayerId(playerId);
        gameRepo.save(game);
    }

    /**
     * Returns the Player ID of the following Player. Return value depends on type of Turn.
     * @return Player ID for the following Player
     */
    protected Long getNextPlayer() {
        Long nextPlayerId = -1L;

        // can be replaced by factory pattern!
        switch(round.getTurns().get( round.getCurrentTurnIndex() )) {
            case NORMAL:
                nextPlayerId = getPlayerForNormalTurn( game.getCurrentPlayerId() );
                break;

            case HIDDEN:
                nextPlayerId = getPlayerForNormalTurn( game.getCurrentPlayerId() );
                break;

            case DOUBLE_TURNS:
                nextPlayerId = getPlayerForDoubleTurn( game.getCurrentPlayerId() );
                break;

            case REVERSE:
                nextPlayerId = getPlayerForReverseTurn( game.getCurrentPlayerId() );
                break;

            default:
                break;
        }

        return nextPlayerId;
    }

    protected Long getPlayerForNormalTurn(Long currentPlayerId) {
        List<Player> players = game.getPlayers();
        currentPlayer = playerRepo.findOne(currentPlayerId);

        if( (players.indexOf( currentPlayer ) + 1) == players.size()) {
            //at end of List, next Player will be at Index 0
            return players.get(0).getId();
        } else {
            //if currentPlayer not at the end of the list (1 because of indices starting at 0)
            return players.get( players.indexOf( currentPlayer ) + 1).getId();
        }
    }

    protected Long getPlayerForDoubleTurn(Long currentPlayerId) {
        throw new NotYetImplementedException("getPlayerForDoubleTurn() is missing");


    }

    protected Long getPlayerForReverseTurn(Long currentPlayerId) {
        List<Player> players = game.getPlayers();
        currentPlayer = playerRepo.findOne(currentPlayerId);
        Integer listEnd = players.size() - 1;

        if( (players.indexOf( currentPlayer ) - 1) < 0 ) {
            //at beginning of List, next Player will be at end of list
            return players.get( listEnd ).getId();
        } else {
            //if currentPlayer not at the end of the list (1 because of indices starting at 0)
            return players.get( players.indexOf( currentPlayer ) - 1).getId();
        }
    }

    protected boolean isGameOver(Integer nthround) {
        // initialize helper variables
        Integer lastTurnIndex = round.getTurns().size() - 1;
        Integer lastPlayerIndex = players.size() - 1;
        Long lastPlayerId = players.get( lastPlayerIndex ).getId();

        // check for end of Turn (only works for Normal Round!)
        if( game.getCurrentPlayerId() == lastPlayerId ) {
            // TODO: execute ActionPhase

            // check for end of Round
            if( round.getCurrentTurnIndex() == lastTurnIndex ) {
                // TODO: execute Round End Event
                // TODO: increment nthRound

                // check for end of Game
                if( nthround > RoundConfigurator.MAX_ROUNDS_FOR_GAME) {
                    // TODO: end game
                    return true;
                }

            }
        }

        return false;
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
        //setCurrentPlayer(game.getId(), nthround, cardStack.peekFirst().getOwnerId());
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
