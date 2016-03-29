package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.constant.GameConstants;
import ch.uzh.ifi.seal.soprafs16.constant.LootType;
import ch.uzh.ifi.seal.soprafs16.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs16.model.*;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.LootRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by soyabeen on 24.03.16.
 */
@Service("playerService")
public class PlayerService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerService.class);

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private CharacterService characterService;

    @Autowired
    private LootRepository lootRepo;

    /**
     * Lists all players for a given game.
     * @param gameId
     * @return
     */
    public List<Player> listPlayersForGame(long gameId) {
        List<Player> result = new ArrayList<>();

        Game game = gameRepo.findOne(gameId);

        if (game != null) {
            result.addAll(game.getUsers().stream().map(User::getPlayer).collect(Collectors.toList()));
        } else {
            logger.error("No game found for id: " + gameId);
        }
        return result;
    }

    /**
     * Creates a new player for a given user and adds this player to the game.
     * @param gameId The game to which we want to add a player.
     * @param userToken User identifying token.
     * @param character User's chosen character.
     * @return The id of the newly created player
     */
    public Long createPlayerForUser(Long gameId, String userToken, Character character) {
        logger.debug("createPlayerForUser");

        Game game = gameRepo.findOne(gameId);

        if (game != null && GameConstants.MAX_PLAYERS >= game.getNumberOfPlayers()
                && characterService.listAvailableCharactersByGame(gameId).contains(character)) {
            // get User
            User user = userRepo.findByToken(userToken);

            // create new player for user
            Player player = createPlayer(character);

            // assign Player to user
            assignPlayerToUser(user, player);

            // assign user to game
            game.addUser(user);
            gameRepo.save(game);

            return user.getPlayer().getId();
        } else {
            //TODO: Find better way to handle this
            logger.error("No game found/game is full/character not available for id: " + gameId);
        }

        return -1L;
    }

    /**
     * Assing player to a user.
     * @param user
     * @param player
     */
    protected void assignPlayerToUser(User user, Player player) {
        if (user.getPlayer() == null) {
            user.setPlayer(player);
            user.setStatus(UserStatus.ONLINE);
        } else {
            logger.error("User already has Player assigned");
        }

        user = userRepo.save(user);
    }

    /**
     * Creates a new Player with given character.
     * @param character Users's chosen character.
     * @return new Player.
     */
    protected Player createPlayer(Character character) {
        Loot loot = new Loot(LootType.PURSE, 250, Positionable.Level.BOTTOM);
        loot = lootRepo.save(loot);

        Player player = new Player(loot);
        player.setCharacter(character);
        player = playerRepo.save(player);

        return player;
    }

}
