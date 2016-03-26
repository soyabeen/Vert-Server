package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.UserRepository;
import ch.uzh.ifi.seal.soprafs16.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by antoio on 3/26/16.
 */
@RestController
public class GamePlayerServiceController
        extends GenericService {

    Logger  logger = LoggerFactory.getLogger(GamePlayerServiceController.class);


    @Autowired
    public PlayerService playerService;

    private final String CONTEXT = "/games/{gameId}/players";

    @RequestMapping(value = CONTEXT, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String createPlayerForGame(@PathVariable Long gameId, @RequestParam("token") String userToken) {
        return CONTEXT + "/" + gameId + "/player/" + ( playerService.createPlayerForGame(gameId, userToken) );
    }


}
