package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.GameConstants;
import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soyabeen on 24.03.16.
 */
@Service("playerService")
public class PlayerService {

    Logger logger = LoggerFactory.getLogger(PlayerService.class);

    @Autowired
    private GameRepository gameRepo;
    @Autowired
    private UserRepository userRepo;

    public List<Player> listPlayersForGame(long gameId) {
        logger.debug("listPlayers" + ": " + String.valueOf(gameId) );
        List<Player> result = new ArrayList<>();
        for (User user : gameRepo.findOne(gameId).getUsers() ) {
            result.add(user.getPlayer());
        }
        return result;
    }


    public String createPlayerForGame(long gameId, String userToken, Player playerchar) {
        logger.debug("addPlayer: " + userToken);

        Game game = gameRepo.findOne(gameId);
        User player = userRepo.findByToken(userToken);

        if (game != null && player != null
                && game.getUsers().size() < GameConstants.MAX_PLAYERS) {

           game.getUsers().add(player);

            logger.debug("Game: " + game.getName() + " - player added: " + player.getUsername());
            return String.valueOf((game.getUsers().size()));
        } else {
            logger.error("Error adding player with token: " + userToken);
        }
        return null;
    }
}
