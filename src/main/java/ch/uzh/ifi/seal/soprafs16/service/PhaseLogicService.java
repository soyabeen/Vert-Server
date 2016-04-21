package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.RoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by antoio on 4/20/16.
 */
@Service("PhaseLogicService")
public class PhaseLogicService {

    @Autowired
    GameRepository gameRepo;

    @Autowired
    RoundRepository roundRepo;

    @Autowired
    PlayerRepository playerRepo;


    /**
     * Returns the Player ID of the player following the current player
     * @param gameId
     * @return Player ID for the following Player
     */
    public Long getNextPlayer(Long gameId) {
        Game game = gameRepo.findOne(gameId);

        // return next player
        return game.getNextPlayerId();
    }

    public Long getStartPlayer(Long gameId) {

        return -1L;
    }
}
