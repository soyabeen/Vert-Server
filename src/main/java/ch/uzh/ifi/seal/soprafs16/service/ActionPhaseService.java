package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.constant.Direction;
import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs16.dto.TurnDTO;
import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.engine.GameEngine;
import ch.uzh.ifi.seal.soprafs16.model.*;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.LootRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.RoundRepository;
import ch.uzh.ifi.seal.soprafs16.service.roundend.RoundEnd;
import ch.uzh.ifi.seal.soprafs16.service.roundend.RoundEndFactory;
import ch.uzh.ifi.seal.soprafs16.utils.RoundConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mirkorichter on 04.05.16.
 */
@Service("actionPhaseService")
public class ActionPhaseService {

    private static final Logger logger = LoggerFactory.getLogger(ActionPhaseService.class);


    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private RoundRepository roundRepo;

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private LootRepository lootRepo;

    @Autowired
    private PhaseLogicService logicService;

    ActionPhaseService() {
        logicService = new PhaseLogicService();
    }

    /**
     * Send the calculated possbilities from the rule engine to the client.
     *
     * @return object with possible selections for the user
     */
    public TurnDTO sendPossibilities(Long gameId) {

        TurnDTO possibilitites = new TurnDTO();

        GameEngine gameEngine = new GameEngine();
        Game game = gameRepo.findOne(gameId);
        Round round = roundRepo.findByGameIdAndNthRound(gameId, game.getRoundId());
        CardType type = round.getCardStack().get(round.getPointerOnDeck()).getType();

        possibilitites.setType(type);

        ActionCommand actionCommand = new ActionCommand(type, game,
                playerRepo.findOne(game.getCurrentPlayerId()), null);

        try {

            List<Positionable> positionables = new ArrayList<>(gameEngine.simulateAction(actionCommand));
            possibilitites.addPlayersAsList(getPlayersFromPositionableList(positionables));
            possibilitites.addLootsAsList(getLootsFromPositionableList(positionables));
            possibilitites.addMarshalAsList(getMarshalFromPositionableList(positionables));

        } catch (InvocationTargetException e) {
            logger.error("Get possibilities from GameEngine failed because of ", e);
            throw new IllegalStateException("Get possibilities from GameEngine failed - IllegalState", e.getCause());
        }

        return possibilitites;


    }

    public void executeDTO(Long gameId, TurnDTO turnDTO) {
        GameEngine gameEngine = new GameEngine();
        Game game = gameRepo.findOne(gameId);
        Round round = roundRepo.findByGameIdAndNthRound(gameId, game.getRoundId());
        CardType type = round.getCardStack().get(round.getPointerOnDeck()).getType();

        if (!type.equals(CardType.DRAW) && !hasNoTarget(turnDTO)) {

            ActionCommand actionCommand;

            //special marshal card
            if (type.equals(CardType.MARSHAL)) {
                Positionable marshalCurr = new Marshal(game.getPositionMarshal());
                Positionable marshalTarget = new Marshal(turnDTO.getMarshal().get(0));
                actionCommand = new ActionCommand(type, game,
                        marshalCurr, marshalTarget);
            } else {

                if (type.equals(CardType.ROBBERY)) {
                    actionCommand = new ActionCommand(type, game,
                            playerRepo.findOne(game.getCurrentPlayerId()), null);

                    // lootId can be null, eg. if the player has no loot, or the car floor is empty
                    if (turnDTO.getLootId() != null) {
                        actionCommand.setTargetLoot(lootRepo.findOne(turnDTO.getLootId()));
                    }
                } else if (turnDTO.getPlayers().get(0) == null) {
                    Player targetPlayer = turnDTO.getPlayers().get(0);
                    targetPlayer.setId(game.getCurrentPlayerId());
                    actionCommand = new ActionCommand(type, game,
                            playerRepo.findOne(game.getCurrentPlayerId()), targetPlayer);
                } else if (type.equals(CardType.PUNCH)) {
                    actionCommand = new ActionCommand(type, game,
                            playerRepo.findOne(game.getCurrentPlayerId()), turnDTO.getPlayers().get(0));
                    actionCommand.setDirection(turnDTO.isPunchRight() ? Direction.TO_HEAD : Direction.TO_TAIL);
                    // lootId can be null, eg. if the player has no loot, or the car floor is empty
                    if (turnDTO.getLootId() != null) {
                        actionCommand.setTargetLoot(lootRepo.findOne(turnDTO.getLootId()));
                    }
                } else {
                    actionCommand = new ActionCommand(type, game,
                            playerRepo.findOne(game.getCurrentPlayerId()), turnDTO.getPlayers().get(0));
                }
            }
            List<Player> players;
            List<Loot> loots;
            List<Marshal> marshal;

            try {
                logger.debug("action command: card {} , actor {}, target {}, lootid {}",
                        actionCommand.getCard(), actionCommand.getCurrentPlayer(), actionCommand.getTargetPlayer(),
                        actionCommand.getTargetLoot());
                ArrayList<Positionable> positionables = new ArrayList<>(gameEngine.executeAction(actionCommand));

                for (Positionable p : positionables) {
                    logger.debug(p.toString());
                }

                players = getPlayersFromPositionableList(positionables);
                loots = getLootsFromPositionableList(positionables);
                marshal = getMarshalFromPositionableList(positionables);

            } catch (InvocationTargetException e) {
                logger.error("Get update from GameEngine failed.", e);
                throw new IllegalStateException("Get update from GameEngine failed - IllegalState", e.getCause());
            }

            //update players and loots
            for (Player p : players) {
                updatePlayer((p.getId() == null) ? playerRepo.findOne(game.getCurrentPlayerId()).getId() : p.getId(), p);
            }
            for (Loot l : loots) {
                updateLoot(l.getId(), l);
            }

            if (marshal.size() != 0) {
                game.setPositionMarshal(marshal.get(0).getCar());
            }
        }

        Game g = setNextPlayerAndChangeState(game, round);

        roundRepo.save(round);
        gameRepo.save(g);


    }

    /**
     * Extracts Player from an positionable list
     *
     * @param positionables List of players and loots
     * @return players from the list
     */
    private List<Player> getPlayersFromPositionableList(List<Positionable> positionables) {

        List<Player> players = new ArrayList<>();

        for (Positionable pos : positionables) {
            if (pos instanceof Player) {
                players.add((Player) pos);
            }
        }
        return players;
    }


    /**
     * Extracts Marshal from an positionable list
     *
     * @param positionables List of players and loots
     * @return marshal in list
     */
    private List<Marshal> getMarshalFromPositionableList(List<Positionable> positionables) {
        List<Marshal> marshals = new ArrayList<>();

        for (Positionable pos : positionables) {
            if (pos instanceof Marshal) {
                marshals.add((Marshal) pos);
            }
        }
        return marshals;
    }


    /**
     * Extracts Loot from an positionable list
     *
     * @param positionables List of players and loots
     * @return loots from the list
     */
    private List<Loot> getLootsFromPositionableList(List<Positionable> positionables) {

        List<Loot> loots = new ArrayList<>();

        for (Positionable pos : positionables) {
            if (pos instanceof Loot) {
                loots.add((Loot) pos);
            }
        }

        return loots;
    }


    /**
     * Updates a player in the database
     *
     * @param oldPlayerId   id of to be updated player
     * @param updatedPlayer player object with updated attributes
     * @return updated and saved Player
     */
    private Player updatePlayer(Long oldPlayerId, Player updatedPlayer) {
        Player player = playerRepo.findOne(oldPlayerId);
        player.update(updatedPlayer);
        return playerRepo.save(player);
    }

    /**
     * Updates a loot in the database
     *
     * @param oldLootId   id of the to be updated loot
     * @param updatedLoot updated loot object
     * @return updated and saved loot
     */
    private Loot updateLoot(Long oldLootId, Loot updatedLoot) {
        Loot loot = lootRepo.findOne(oldLootId);
        loot.update(updatedLoot);
        return lootRepo.save(loot);
    }

    private void updatePositionableList(Game game, List<Positionable> positionables) {
        List<Player> players = getPlayersFromPositionableList(positionables);
        List<Loot> loots = getLootsFromPositionableList(positionables);
        List<Marshal> marshal = getMarshalFromPositionableList(positionables);

        for (Player p : players) {
            updatePlayer((p.getId() == null) ? playerRepo.findOne(game.getCurrentPlayerId()).getId() : p.getId(), p);
        }
        for (Loot l : loots) {
            if (null == l.getId()) {
                lootRepo.save(l);
            } else {
                updateLoot(l.getId(), l);
            }
        }
        if (marshal.size() != 0) {
            game.setPositionMarshal(marshal.get(0).getCar());
        }

    }

    /**
     * Set next player and
     * Transition form Action- to PlanningPhase and EndOfGame
     *
     * @param game
     * @param round
     */
    private Game setNextPlayerAndChangeState(Game game, Round round) {
        round.incrementPointer();
        if (round.getPointerOnDeck() < round.getCardStack().size()) {
            // as long as we haven't played the last card, we set the next player
            game.setCurrentPlayerId(round.getCardStack().
                    get(round.getPointerOnDeck()).getOwnerId());
        } else {
            // execute the round end event, when all player actions have been processed.
            RoundEnd rd = RoundEndFactory.chooseEnd(round.getEnd());
            List<Positionable> positionables = rd.execute(game);
            if (positionables.size() != 0) {
                updatePositionableList(game, positionables);
            }

            // set status to finished if last round, otherwise start new round
            if (isGameInLastRound(game)) {
                game.setStatus(GameStatus.FINISHED);
            } else {
                game.startNewRound();
            }
        }
        return game;
    }

    private boolean hasNoTarget(TurnDTO dto) {
        if ((dto.getType().equals(CardType.FIRE) || dto.getType().equals(CardType.PUNCH))
                && dto.getPlayers().size() == 0) {
            return true;
        }
        return false;

    }

    private boolean isGameInLastRound(Game game) {
        List<Round> roundsForGame = roundRepo.findByGameId(game.getId());
        int maxRounds = roundsForGame != null && !roundsForGame.isEmpty() ? roundsForGame.size() : RoundConfigurator.MAX_ROUNDS_FOR_GAME + 1;
        return game.getRoundId() == maxRounds;
    }



}
