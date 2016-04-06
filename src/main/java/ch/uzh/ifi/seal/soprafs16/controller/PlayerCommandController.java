package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.model.repositories.UserRepository;
import ch.uzh.ifi.seal.soprafs16.service.PlayerService;
import ch.uzh.ifi.seal.soprafs16.utils.InputArgValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by antoio on 3/26/16.
 */
@RestController
public class PlayerCommandController
        extends GenericController {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(PlayerCommandController.class);

    @Autowired
    private PlayerService playerService;

    @Autowired
    private UserRepository userRepo;

    private static final String CONTEXT = "/games/{gameId}/players";

    @RequestMapping(value = CONTEXT, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String createPlayerForGame(@PathVariable Long gameId, @RequestParam("token") String userToken,
                                      @RequestParam("character") Character character) {

        User tokenOwner = InputArgValidator.checkTokenHasValidUser(userToken, userRepo, "token");

        //TODO: add full link or relative URL
        return playerService.createPlayerForUser(gameId, tokenOwner, character).toString();
    }
}
