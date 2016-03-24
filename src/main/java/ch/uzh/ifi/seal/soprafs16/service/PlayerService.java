package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soyabeen on 24.03.16.
 */
@Service("playerService")
public class PlayerService {

    @Autowired
    private GameRepository gameRepo;

    public List<Player> listPlayersForGame(long gameId) {
        List<Player> result = new ArrayList<>();
        for (User user : gameRepo.findOne(gameId).getUsers() ) {
            result.add(user.getPlayer());
        }
        return result;
    }
}
