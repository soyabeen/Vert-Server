package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.utils.GameConfigurator;
import ch.uzh.ifi.seal.soprafs16.constant.LootType;
import ch.uzh.ifi.seal.soprafs16.model.*;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.LootRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
import ch.uzh.ifi.seal.soprafs16.utils.InputArgValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by soyabeen on 24.03.16.
 */
@Service("playerService")
public class PlayerService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerService.class);

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private LootRepository lootRepo;

    @Autowired
    private CharacterService characterService;

    public Player createPlayer(Player player) {
        InputArgValidator.checkNotEmpty(player.getUsername(), "username");
        InputArgValidator.checkUserNameNotUsed(player.getUsername(), playerRepo, "username");

        // Take only the username from the player.
        Player p = new Player();
        p.setUsername(player.getUsername());
        p.setToken(UUID.randomUUID().toString());
        return playerRepo.save(p);
    }

    public List<Player> listPlayers() {
        List<Player> result = new ArrayList<>();
        playerRepo.findAll().forEach(result::add);
        return result;
    }

    public Player getPlayer(Long playerId) {
        return (Player) InputArgValidator.checkAvailabeId(playerId, playerRepo, "playerId");
    }

    /**
     * Lists all players for a given game.
     *
     * @param gameId
     * @return
     */
    public List<Player> listPlayersForGame(long gameId) {
        List<Player> result = new ArrayList<>();

        Game game = gameRepo.findOne(gameId);

        if (game != null) {
            logger.debug("Game id: " + game.getId());
            logger.debug("Number of player in game: " + game.getPlayers().size());
            result.addAll(game.getPlayers());
        } else {
            logger.error("No game found for id: " + gameId);
        }
        return result;
    }

    /**
     * Creates a new player for a given user and adds this player to the game.
     *
     * @param gameId    The game to which we want to add a player.
     * @param player    The user owning the player.
     * @param character User's chosen character.
     * @return The id of the newly created player
     */
    public Player assignPlayer(Long gameId, Player player, Character character) {
        logger.debug("assignPlayer");

        InputArgValidator.checkIfPositiveNumber(gameId, "gameid");
        InputArgValidator.checkNotNull(player, "player");
        InputArgValidator.checkNotNull(character, "character");

        Game game = (Game) InputArgValidator.checkAvailabeId(gameId, gameRepo, "gameid");

        if (game != null && game.getPlayers().size() < GameConfigurator.MAX_PLAYERS
                && characterService.listAvailableCharactersByGame(gameId).contains(character)) {

            // create new player for user
            player = initializeCharacter(gameId, player, character);

            // assign user to game
            game.addPlayer(player);
            gameRepo.save(game);

            return player;
        } else {
            //TODO: Find better way to handle this
            logger.error("No game found/game is full/character not available for id: " + gameId);
        }

        return new Player();
    }

    /**
     * Creates a new Player with given character.
     *
     * @param character Users's chosen character.
     * @return new Player.
     */
    public Player initializeCharacter(Long gameId, Player player, Character character) {

        Game game = (Game) InputArgValidator.checkAvailabeId(gameId, gameRepo, "gameid");

        // give player the start loot
        Loot loot = new Loot(LootType.PURSE_SMALL, gameId, LootType.PURSE_SMALL.value());
        loot = lootRepo.save(loot);
        player.addLoot(loot);

        player.setCharacter(character);
        return playerRepo.save(player);
    }


}
