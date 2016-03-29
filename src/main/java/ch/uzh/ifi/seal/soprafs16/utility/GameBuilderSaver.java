package ch.uzh.ifi.seal.soprafs16.utility;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by alexanderhofmann on 29/03/16.
 */
@Service
public class GameBuilderSaver {

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PlayerRepository playerRepo;


    public Game saveGame(Game game) {
        for (int i = 0; i < game.getUsers().size(); i++) {
            User user = game.getUsers().get(i);
            // save all players
            user.setPlayer(playerRepo.save(user.getPlayer()));
            // save all users
            userRepo.save(user);
        }
        // save the game
        return gameRepo.save(game);
    }
}
