package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexanderhofmann on 25/03/16.
 */
@Service("positionService")
public class PositionService {

    private static final Logger logger  = LoggerFactory.getLogger(PositionService.class);

    @Autowired
    private PlayerService playerService;

    @Autowired
    private LootService lootService;

    public List<Positionable> listPositionablesForGame(Long gameId) {
        List<Positionable> positionables = new ArrayList<>();

        positionables.addAll(playerService.listPlayersForGame(gameId));
        positionables.addAll(lootService.listLootsForGame(gameId));

        return positionables;
    }
}
