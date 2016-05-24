package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs16.constant.Turn;
import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.model.Card;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Round;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.LootRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.RoundRepository;
import ch.uzh.ifi.seal.soprafs16.utils.InputArgValidator;
import ch.uzh.ifi.seal.soprafs16.utils.RoundConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired
    LootRepository lootRepo;


    public Long getInitialPlayerId(Game game) {
        return game.getCurrentPlayerId() == null || game.getCurrentPlayerId() <= 0
                ? game.getPlayers().get(0).getId()
                : game.getCurrentPlayerId();
    }

    /**
     * This method is the single entry point to the Business Logic.
     *
     * @param gameId
     * @param nthround
     */
    public void advancePlayer(Long gameId, Integer nthround) {
        InputArgValidator.checkIfPositiveNumber(gameId, "gameId");
        InputArgValidator.checkIfPositiveNumber(nthround.longValue(), "nthRound");

        // initialize needed repositories
        Game game = gameRepo.findOne(gameId);

        //checkGameChangeState(game, nthround);
        if(game.getStatus() == GameStatus.PLANNINGPHASE) {
            game.setCurrentPlayerId(getNextPlayer(game, nthround));
            changeState(game, nthround);
        }

        // save repositories
        gameRepo.save(game);
    }

    protected void setCurrentPlayerId(Long gameId, Long playerId) {
        InputArgValidator.checkAvailabeId(gameId, gameRepo, "Given gameId is no valid game for method " +
                "\'setCurrentPlayer()\' in PhaseLogicService");
        InputArgValidator.checkAvailabeId(playerId, playerRepo, "Given playerId is no valid player for method " +
                "\'setCurrentPlayer()\' in PhaseLogicService");

        Game game = gameRepo.findOne(gameId);
        game.setCurrentPlayerId(playerId);
        gameRepo.save(game);
    }

    /**
     * Returns the Player ID of the following Player. Return value depends on type of Turn.
     *
     * @return Player ID for the following Player
     */
    protected Long getNextPlayer(Game game, Integer nthround) {
        Long nextPlayerId = -1L;

        Round round = roundRepo.findByGameIdAndNthRound(game.getId(), nthround);

        switch ( round.getTurns().get(game.getTurnId()-1) ) {
            case DOUBLE_TURNS:
                nextPlayerId = getPlayerForDoubleTurn(game, round);
                break;

            case REVERSE:
                nextPlayerId = getPlayerForReverseTurn(game);
                break;

            default:
                nextPlayerId = getPlayerForNormalTurn(game);
                break;
        }

        return nextPlayerId;
    }

    /**
     * Method used to access player Ids in List of players in Game.
     * List of players in Game can't be accessed directly.
     * @param players
     * @return
     */
    protected List<Long> getListOfPlayerIds(List<Player> players) {
        ArrayList<Long> playerIds = new ArrayList<>();
        for (Player p : players) {
            playerIds.add(p.getId());
        }
        return playerIds;
    }

    protected Long getPlayerForNormalTurn(Game game) {
        List<Player> players = game.getPlayers();
        List<Long> playerIds = getListOfPlayerIds(players);

        Player currentPlayer = playerRepo.findOne(game.getCurrentPlayerId());

        int nextPlayerIndex = playerIds.indexOf(currentPlayer.getId()) + 1;

        if (nextPlayerIndex == players.size()) {
            //at end of List, next Player will be at Index 0
            return players.get(0).getId();
        } else {
            //if currentPlayer not at the end of the list (1 because of indices starting at 0)
            return players.get(nextPlayerIndex).getId();
        }
    }

    protected Long getPlayerForDoubleTurn(Game game, Round round) {
        // for double turn use already played cards in stack
        // look at how many cards are in stack
        // if 1 card is in stack, leave current player
        // if more than 1 is in stack look at last two cards
        // if owner of last 2 cards are different leave current Player else change Player according to Normal Turn

        Player currentPlayer = playerRepo.findOne(game.getCurrentPlayerId());
        List<Card> stack = round.getCardStack();

        if(stack.size() > 1)
            if(stack.get(stack.size()-2).getOwnerId().equals(currentPlayer.getId()))
                return getPlayerForNormalTurn(game);
            else
                return currentPlayer.getId();
        else
            return getPlayerForNormalTurn(game);

    }

    protected Long getPlayerForReverseTurn(Game game) {
        List<Player> players = game.getPlayers();
        Player currentPlayer = playerRepo.findOne(game.getCurrentPlayerId());
        // to access players of game use getListOfPlayersIds(...)
        Integer listEnd = players.size() - 1;

        if ((players.indexOf(currentPlayer) - 1) < 0) {
            //at beginning of List, next Player will be at end of list
            return players.get(listEnd).getId();
        } else {
            //if currentPlayer not at the end of the list (1 because of indices starting at 0)
            return players.get(players.indexOf(currentPlayer) - 1).getId();
        }
    }

    protected void changeState(Game game, Integer nthround) {
        // Order in which the game should be checked:
        // if Turn is over execute Action Phase
        // if Action Phase is over check if Round is over else start new turn
        // if Round is over execute Round End Event and check if game is over else start new Round
        // if game is over collect all information and end game


        Round round = roundRepo.findByGameIdAndNthRound(game.getId(),nthround);
        if (isTurnOver(game, round)) {
            logger.debug("Game " + game.getId() + ": State changed, Turn is over");
            game.setTurnId(game.getTurnId() + 1);
        }

        if(isRoundOver(game,round)) {
            logger.debug("Game " + game.getId() + ": State changed, Round is over");
            game.setStatus(GameStatus.ACTIONPHASE);
            round.setPointerOnDeck(0);
        }

        if(isGameOver(nthround)) {
            logger.debug("Game " + game.getId() + ": State changed, Game is over");
            game.setStatus(GameStatus.FINISHED);
        }

        roundRepo.save(round);
        // still in turn, proceed normally
        return;
    }

    protected boolean isTurnOver(Game game, Round round) {
        List<Turn> turns = round.getTurns();
        int stackSize = round.getCardStack().size();
        int nrOfPlayers = game.getPlayers().size();
        int changeTurn = 0;
        for(int i = 0; i < game.getTurnId(); ++i) {
            if(turns.get(i).equals(Turn.DOUBLE_TURNS))
                changeTurn += 2 * nrOfPlayers;
            else changeTurn += nrOfPlayers;
        }
        if(stackSize == changeTurn) return true;
        return false;
    }

    protected boolean isRoundOver(Game game, Round round) {
        Integer lastTurnIndex = round.getTurns().size() + 1;
        if (game.getTurnId() == lastTurnIndex) {
            return true;
        }
        return false;
    }

    protected boolean isGameOver(Integer nthround) {
        //MAX_ROUNDS_FOR_GAME == 4 plus 1 Station Round = 5 Rounds total
        if (nthround > RoundConfigurator.MAX_ROUNDS_FOR_GAME + 1) {
            return true;
        }

        return false;
    }


    /**
     * Called from Client Request
     * @param game
     * @param nthround
     */
    protected void executeActionPhase(Game game, Integer nthround) {
        // setup / prepare Phase
        Round round = roundRepo.findByGameIdAndNthRound(game.getId(), nthround);
        LinkedList<Card> cardStack = (LinkedList<Card>) round.getCardStack();
        Card topCard;

        // peek and set CurrentPlayer

        // Program progress is driven ON REQUEST
        if(!cardStack.isEmpty()) {
            // setup of action phase

            topCard = cardStack.peekFirst();
            setCurrentPlayerId(game.getId(), topCard.getOwnerId());

            // give Card to Rule Engine
            ActionCommand result = receivePossibilities(topCard);


            //ON REQUEST return something for client
            // fixme: evaluate result from Rule Engine
            evaluateResultRuleEngine(result);

            // remove topCard from Stack
            cardStack.pollFirst();
        }


        // increment nthRound
        round.incrementNthRound();

        //save repos
        roundRepo.save(round);
    }

    /**
     * Determine whether result from Rule Engine needs a decision from player or if game can be updated directly.
     *
     * @param result computed result from Rule Engine
     */
    protected void evaluateResultRuleEngine(ActionCommand result) {
        throw new IllegalStateException("Not yet implemented");

        // update Game
        // or
        // send result from Rule Engine to Client
    }

    /**
     * Start computing possibilities for Player.
     *
     * @param topCard
     */
    protected ActionCommand receivePossibilities(Card topCard) {
        throw new IllegalStateException("Not yet implemented");

        // give played card to rule engine

        // give back result
        // return result;

    }

    /**
     *
     * @param game
     * @param round
     * @return
     */
    private int setTurnId(Game game, Round round) {
        List<Turn> turns = round.getTurns();
        int stackSize = round.getCardStack().size();
        int nrOfPlayers = game.getPlayers().size();
        int newTurnId = (stackSize / nrOfPlayers);
        if(!turns.contains(Turn.DOUBLE_TURNS) || turns.indexOf(Turn.DOUBLE_TURNS) + 1 > newTurnId)
            return newTurnId + 1;
        else return newTurnId;

    }


}
