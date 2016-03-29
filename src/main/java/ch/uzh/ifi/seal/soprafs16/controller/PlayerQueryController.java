package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.service.GenericService;
import ch.uzh.ifi.seal.soprafs16.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by antoio on 3/26/16.
 */
@RestController
public class PlayerQueryController
        extends GenericService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerQueryController.class);

    @Autowired
    public PlayerService playerService;

    private final String CONTEXT = "/games/{gameId}/players";

    @RequestMapping(value = CONTEXT, method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public List<Player> listPlayersForGame(@PathVariable Long gameId) {
        return playerService.listPlayersForGame(gameId);
    }
}
