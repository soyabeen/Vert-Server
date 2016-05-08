package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.dto.TurnDTO;
import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.engine.GameEngine;
import ch.uzh.ifi.seal.soprafs16.exception.InvalidInputException;
import ch.uzh.ifi.seal.soprafs16.model.*;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.LootRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.RoundRepository;
import org.jboss.logging.annotations.Pos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
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

    ActionPhaseService() {logicService = new PhaseLogicService();}

    /**
     * Send the calculated possbilities from the rule engine to the client.
     *
     * @return object with possible selections for the user
     */
    public TurnDTO sendPossibilities(Long gameId) {

        TurnDTO possibilitites = new TurnDTO();

        GameEngine gameEngine = new GameEngine();
        Game game = gameRepo.findOne(gameId);
        Round round = roundRepo.findByGameIdAndNthRound(gameId,game.getRoundId());
        LinkedList<Card> stack = new LinkedList<>(round.getCardStack());
        CardType type = stack.peekFirst().getType();

        possibilitites.setType(type);

        ActionCommand actionCommand = new ActionCommand(type, game,
                playerRepo.findOne(game.getCurrentPlayerId()), null);

        try {

            List<Positionable> positionables = new ArrayList<>(gameEngine.simulateAction(actionCommand));
            possibilitites.addPlayersAsList(getPlayersFromPositionableList(positionables) );
            possibilitites.addLootsAsList(getLootsFromPositionableList(positionables));
            possibilitites.setPositionMarshal(getMarshalFromPositionableList(positionables).getCar());

        } catch (InvocationTargetException e) {
            //TODO: Exception handling
            throw new IllegalStateException("Get possibilities from GameEngine failed");
        }

        return possibilitites;


    }

    public void executeDTO(Long gameId, TurnDTO turnDTO) {
        GameEngine gameEngine = new GameEngine();
        Game game = gameRepo.findOne(gameId);
        Round round = roundRepo.findByGameIdAndNthRound(gameId,game.getRoundId());
        LinkedList<Card> stack = new LinkedList<>(round.getCardStack());
        //This removes the first card via poll
        CardType type = stack.pollFirst().getType();



        List<Player> chosenPossibility = turnDTO.getPlayers();
        Long id = chosenPossibility.get(0).getId();

        //Is it possible to get more than one player back?
        Player targetPlayer = turnDTO.getPlayers().get(0);

        List<Player> players;
        List<Loot> loots;
        Marshal marshal;

        //special marshal card
        if(type.equals(CardType.MARSHAL)) {
            game.setPositionMarshal(targetPlayer.getCar());
        }

        ActionCommand actionCommand = new ActionCommand(type, game,
                playerRepo.findOne(game.getCurrentPlayerId()), targetPlayer);
        try {
            ArrayList<Positionable> positionables = new ArrayList<>(gameEngine.executeAction(actionCommand));

            for(Positionable p: positionables) {
                logger.debug(p.toString());
            }

            players = getPlayersFromPositionableList(positionables);
            loots = getLootsFromPositionableList(positionables);
            marshal = getMarshalFromPositionableList(positionables);

        } catch (InvocationTargetException e) {
            //TODO: Exception handling
            throw new IllegalStateException("Get update from GameEngine failed");
        }

        //update players and loots
        for(Player p: players) {
            updatePlayer((p.getId() == null) ? playerRepo.findOne(game.getCurrentPlayerId()).getId(): p.getId(), p);
        }
        for(Loot l : loots) { updateLoot(l.getId(), l); }

        if(null != marshal) {
            game.setPositionMarshal(marshal.getCar());
        }

        gameRepo.save(game);

        //Usage of logic service
        logicService.advancePlayer(gameId, game.getRoundId());


    }

    /**
     * Extracts Player from an positionable list
     * @param positionables List of players and loots
     * @return players from the list
     */
    private List<Player> getPlayersFromPositionableList(List<Positionable> positionables) {

        List<Player> players = new ArrayList<>();

        for(Positionable pos : positionables) {
            if (pos instanceof Player) {
                players.add((Player) pos);
            } else if (pos instanceof Loot) {
                continue;
            } else if (pos instanceof Marshal) {
                continue;
            } else {
                throw new InvalidInputException("DTO has Unknown positionable object (no palyer/loot)");
            }
        }
        return players;
    }


    /**
     * Extracts Marshal from an positionable list
     * @param positionables List of players and loots
     * @return marshal in list
     */
    private Marshal getMarshalFromPositionableList(List<Positionable> positionables) {

        for(Positionable pos : positionables) {
            if (pos instanceof Marshal) {
                return (Marshal) pos;
            } else if (pos instanceof Loot) {
                continue;
            } else if (pos instanceof Player) {
                continue;
            } else {
                throw new InvalidInputException("DTO has Unknown positionable object (no palyer/loot)");
            }
        }
        return null;
    }


    /**
     * Extracts Loot from an positionable list
     * @param positionables List of players and loots
     * @return loots from the list
     */
    private List<Loot> getLootsFromPositionableList(List<Positionable> positionables) {

        List<Loot> loots = new ArrayList<>();

        for(Positionable pos : positionables) {
            if (pos instanceof Loot) {
                loots.add((Loot) pos);
            } else if (pos instanceof Player) {
                continue;
            } else if (pos instanceof Marshal) {
                continue;
            } else {
                throw new InvalidInputException("DTO has Unknown positionable object (no palyer/loot)");
            }
        }

        return loots;
    }


    /**
     * Updates a player in the database
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
     * @param oldLootId id of the to be updated loot
     * @param updatedLoot updated loot object
     * @return updated and saved loot
     */
    private Loot updateLoot(Long oldLootId, Loot updatedLoot) {
        Loot loot = lootRepo.findOne(oldLootId);
        loot = updatedLoot;
        return lootRepo.save(loot);
    }


}
