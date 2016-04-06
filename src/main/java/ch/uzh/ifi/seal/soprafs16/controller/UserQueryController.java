package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class UserQueryController
        extends GenericController {

    private static final Logger logger = LoggerFactory.getLogger(UserQueryController.class);

    static final String CONTEXT = "/users";

    @Autowired
    private PlayerService playerService;

    @RequestMapping(method = RequestMethod.GET, value = CONTEXT)
    @ResponseStatus(HttpStatus.OK)
    public List<Player> listPlayers() {
        logger.debug("listPlayers");
        return playerService.listPlayers();
    }

    @RequestMapping(method = RequestMethod.GET, value = CONTEXT + "/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Player getUser(@PathVariable Long userId) {
        logger.debug("getPlayer: " + userId);

        return playerService.getPlayer(userId);
    }
}
