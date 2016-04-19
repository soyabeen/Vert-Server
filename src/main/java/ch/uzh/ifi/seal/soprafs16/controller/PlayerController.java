package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
import ch.uzh.ifi.seal.soprafs16.service.PlayerService;
import ch.uzh.ifi.seal.soprafs16.utils.InputArgValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST entry point for player services.
 * <p>
 * Created by antoio on 3/26/16.
 */
@RestController
public class PlayerController extends GenericController {

    private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);

    private static final String CONTEXT = "/games/{gameId}/players";

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PlayerRepository playerRepository;

    /**
     * Assigns player to a game. To be used when joining a game.
     * @param gameId
     * @param userToken
     * @param character
     * @return
     */
    @RequestMapping(value = CONTEXT, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Player assignPlayer(@PathVariable Long gameId, @RequestParam("token") String userToken,
                               @RequestParam("character") Character character) {

        logger.debug("POST: assignPlayer - Args. gameId <{}>, userToken <{}>.", gameId, userToken);
        Player tokenOwner = InputArgValidator.checkTokenHasValidPlayer(userToken, playerRepository, "token");
        return playerService.assignPlayer(gameId, tokenOwner, character);
    }


    @RequestMapping(value = CONTEXT, method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Player> listPlayersForGame(@PathVariable Long gameId) {
        logger.debug("GET: listPlayersForGame - Args. gameId <{}>.", gameId);
        return playerService.listPlayersForGame(gameId);
    }

    /**
     * Assign a character to an existing game. To be used only for the game owner.
     * @param gameId
     * @param userToken
     * @param character
     * @return
     */
    @RequestMapping(value = CONTEXT, method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Player assignCharacter(@PathVariable Long gameId, @RequestParam("token") String userToken,
                                  @RequestParam("character") Character character) {
        Player tokenOwner = InputArgValidator.checkTokenHasValidPlayer(userToken, playerRepository, "token");
            return playerService.initializeCharacter(tokenOwner, character);
    }
}
