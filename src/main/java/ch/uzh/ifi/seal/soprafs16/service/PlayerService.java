package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.UserRepository;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public List<Player> listPlayersForGame(long gameId) {
        List<Player> result = new ArrayList<>();

        Game game = gameRepo.findOne(gameId);

        if (game != null) {
            for (User user : gameRepo.findOne(gameId).getUsers()) {
                result.add(user.getPlayer());
            }
        } else {
            logger.error("No game found for id: " + gameId);
        }
        return result;
    }


    public String createPlayerForUser(String userToken, Player playerchar) {
        // assign Player(initialize) to User
        // assign Player to Game

        logger.debug("add Player to user");

        // get User
        User user = userRepo.findByToken(userToken);

        // assign Player
        if(null == user.getPlayer()) {
            user.setPlayer(playerchar);
            user.setStatus(UserStatus.ONLINE);
        } else {
            logger.debug("User had already Player assigned");
        }

        playerRepo.save(playerchar);
        user = userRepo.save(user);

        return String.valueOf(user.getPlayer().getId());
    }

    protected Player createPlayer(JSONPObject character) {
        //initialize Player

        return null;
    }

}
