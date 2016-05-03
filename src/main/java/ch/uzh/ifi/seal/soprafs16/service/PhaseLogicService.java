package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs16.dto.TurnDTO;
import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.engine.GameEngine;
import ch.uzh.ifi.seal.soprafs16.exception.InvalidInputException;
import ch.uzh.ifi.seal.soprafs16.model.*;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.LootRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.RoundRepository;
import ch.uzh.ifi.seal.soprafs16.utils.InputArgValidator;
import ch.uzh.ifi.seal.soprafs16.utils.RoundConfigurator;
import org.hibernate.cfg.NotYetImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
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
        InputArgValidator.checkInputArgsGameIdAndNthRound(gameId, nthround);

        // initialize needed repositories
        Game game = gameRepo.findOne(gameId);

        checkGameChangeState(game, nthround);
        if(game.getStatus() == GameStatus.PLANNINGPHASE) {
            game.setCurrentPlayerId(getNextPlayer(game, nthround));
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

        switch ( round.getTurns().get(round.getCurrentTurnIndex()) ) {
            case NORMAL:
            case HIDDEN:
                nextPlayerId = getPlayerForNormalTurn(game);
                break;

            case DOUBLE_TURNS:
                nextPlayerId = getPlayerForDoubleTurn(game);
                break;

            case REVERSE:
                nextPlayerId = getPlayerForReverseTurn(game);
                break;

            default:
                // TODO: what to do if program gets here?
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

    protected Long getPlayerForDoubleTurn(Game game) {
        throw new NotYetImplementedException("getPlayerForDoubleTurn() is missing");

        // for double turn use already played cards in stack
        // look at how many cards are in stack
        // if 1 card is in stack, leave current player
        // if more than 1 is in stack look at last two cards
        // if owner of last 2 cards are different leave current Player else change Player according to Normal Turn

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

    protected void checkGameChangeState(Game game, Integer nthround) {
        // Order in which the game should be checked:
        // if Turn is over execute Action Phase
        // if Action Phase is over check if Round is over else start new turn
        // if Round is over execute Round End Event and check if game is over else start new Round
        // if game is over collect all information and end game


        if (isTurnOver(game)) {
            logger.debug("Game " + game.getId() + ": State changed, Turn is over");
            game.setStatus(GameStatus.ACTIONPHASE);

        }

        if(isRoundOver(game,nthround)) {
            logger.debug("Game " + game.getId() + ": State changed, Round is over");
            // TODO: execute Round End Event
            // TODO: start new round
            game.setStatus(GameStatus.PLANNINGPHASE);
        }

        if(isGameOver(nthround)) {
            logger.debug("Game " + game.getId() + ": State changed, Game is over");
            game.setStatus(GameStatus.FINISHED);
            // TODO: end game

        }

        // still in turn, proceed normally
        return;
    }

    protected boolean isTurnOver(Game game) {
        Integer lastPlayerIndex = game.getPlayers().size() - 1;
        Long lastPlayerId = game.getPlayers().get(lastPlayerIndex).getId();

        // check for end of Turn (only works for Normal Round!)
        if (game.getCurrentPlayerId() == lastPlayerId) {
            return true;
        }

        return false;
    }

    protected boolean isRoundOver(Game game, Integer nthround) {
        Round round = roundRepo.findByGameIdAndNthRound(game.getId(), nthround);
        Integer lastTurnIndex = round.getTurns().size() - 1;

        if (round.getCurrentTurnIndex() == lastTurnIndex) {
            return true;
        }

        return false;
    }

    protected boolean isGameOver(Integer nthround) {
        //fixme: MAX_ROUNDS_FOR_GAME == 4 plus 1 Station Round = 5 Rounds total
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
     * Send the calculated possbilities from the rule engine to the client.
     *
     * @return object with possible selections for the user
     */
    public TurnDTO sendPossibilities(Long gameId, Integer nthRound) {

        TurnDTO possibilitites = new TurnDTO();

        GameEngine gameEngine = new GameEngine();
        Round round = roundRepo.findByGameIdAndNthRound(gameId,nthRound);
        LinkedList<Card> stack = new LinkedList<>(round.getCardStack());
        CardType type = stack.peekFirst().getType();
        Game game = gameRepo.findOne(gameId);

        possibilitites.setType(type);

        ActionCommand actionCommand = new ActionCommand(type, game,
                playerRepo.findOne(game.getCurrentPlayerId()), null);

        try {

            List<Positionable> positionables = new ArrayList<>(gameEngine.simulateAction(actionCommand));
            possibilitites.addPlayersAsList(getPlayersFromPositionableList(positionables) );
            possibilitites.addLootsAsList(getLootsFromPositionableList(positionables));

        } catch (InvocationTargetException e) {
            //TODO: Exception handling
            throw new IllegalStateException("Get possibilities from GameEngine failed", e);
        }

        return possibilitites;


    }

    public void executeDTO(Long gameId, Integer nthRound, TurnDTO turnDTO) {
        GameEngine gameEngine = new GameEngine();
        Round round = roundRepo.findByGameIdAndNthRound(gameId,nthRound);
        LinkedList<Card> stack = new LinkedList<>(round.getCardStack());
        //This removes the first card via poll
        CardType type = stack.pollFirst().getType();
        Game game = gameRepo.findOne(gameId);

        List<Player> chosenPossibility = turnDTO.getPlayers();
        Long id = chosenPossibility.get(0).getId();

        //Is it possible to get more than one player back?
        Player targetPlayer;
        if(id != null) {
            targetPlayer = playerRepo.findOne(id);
        } else {
            targetPlayer = playerRepo.findOne(game.getCurrentPlayerId());
        }


        /*TODO: for other cardtypes than move
        List<Player> players = new ArrayList<>();
        List<Loot> loots = new ArrayList<>();

        if(targetPlayer != null) {
            ActionCommand actionCommand = new ActionCommand(type, game,
                    playerRepo.findOne(game.getCurrentPlayerId()), targetPlayer);
            try {
                ArrayList<Positionable> positionables = new ArrayList<>(gameEngine.simulateAction(actionCommand));
                players = getPlayersFromPositionableList(positionables);
                loots = getLootsFromPositionableList(positionables);
            } catch (InvocationTargetException e) {
                //TODO: Exception handling
                throw new IllegalStateException("Get possibilities from GameEngine failed");
            }
        } else {
            //TODO: Error handling
        }*/
        targetPlayer.setCar(chosenPossibility.get(0).getCar());
        playerRepo.save(targetPlayer);

        advancePlayer(gameId, nthRound);


    }


    private List<Player> getPlayersFromPositionableList(List<Positionable> positionables) {

        List<Player> players = new ArrayList<>();

        for(Positionable pos : positionables) {
            if (pos instanceof Player) {
                players.add((Player) pos);
            } else if (pos instanceof Loot) {
            } else {
                throw new InvalidInputException("DTO has Unknown positionable object (no palyer/loot)");
            }
        }
        return players;
    }

    private List<Loot> getLootsFromPositionableList(List<Positionable> positionables) {

        List<Loot> loots = new ArrayList<>();

        for(Positionable pos : positionables) {
            if (pos instanceof Loot) {
                loots.add((Loot) pos);
            } else if (pos instanceof Player) {
            } else {
                throw new InvalidInputException("DTO has Unknown positionable object (no palyer/loot)");
            }
        }

        return loots;
    }


}
