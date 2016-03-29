package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.service.PlayerService;
import com.fasterxml.jackson.databind.util.JSONPObject;
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
public class PlayerController
        extends GenericController {

    private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);


    @Autowired
    public PlayerService playerService;

    private final String CONTEXT = "/games/{gameId}/players";

    @RequestMapping(value = CONTEXT, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)

    public String createPlayerForGame(@PathVariable Long gameId, @RequestParam("token") String userToken,
                                      @RequestBody Player playerchar, @RequestBody JSONPObject object) {
        //TODO: What if null was returned from createPlayerForGame()? How do we handle this? (Error Message? Ignore?)
        //return CONTEXT + "/" + gameId + "/player/" + ( playerService.createPlayerForUser(gameId, userToken, playerchar));
        return null;
    }

    @RequestMapping(value = CONTEXT)
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public List<Player> listPlayersForGame(@PathVariable Long gameId) {
        return playerService.listPlayersForGame(gameId);
    }
}
