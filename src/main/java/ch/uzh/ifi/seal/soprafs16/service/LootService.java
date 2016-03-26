package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.model.Loot;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexanderhofmann on 25/03/16.
 */
@Service("lootService")
public class LootService {

    private static final Logger logger = LoggerFactory.getLogger(LootService.class);

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private PlayerService playerService;

    public List<Loot> listLootsForGame(Long gameId) {
        List<Loot> loots = new ArrayList<>();
        loots.addAll(gameRepo.findOne(gameId).getLoots());

        List<Player> players = playerService.listPlayersForGame(gameId);
        for (Player player : players) {
            loots.addAll(player.getLoots());
        }

        return loots;
    }

}
