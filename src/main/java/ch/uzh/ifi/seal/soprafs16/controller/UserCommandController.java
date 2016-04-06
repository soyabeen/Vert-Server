package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Command Controller for the {@code/users} endpoint.
 */
@RestController
public class UserCommandController
        extends GenericController {

    private static final Logger logger = LoggerFactory.getLogger(UserCommandController.class);

    private static final String CONTEXT = "/users";

    @Autowired
    private PlayerService playerService;

    /**
     * Adds a new user to the database.
     * @param player
     * @return
     */
    @RequestMapping(value = CONTEXT, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Player addUser(@RequestBody Player player) {
        logger.debug("POST: - Args. name <{}>, username <{}>.", CONTEXT, player.getUsername());
        return playerService.createPlayer(player);
    }
}
