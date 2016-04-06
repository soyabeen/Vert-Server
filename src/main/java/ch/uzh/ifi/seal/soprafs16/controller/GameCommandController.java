package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.UserRepository;
import ch.uzh.ifi.seal.soprafs16.service.GameService;
import ch.uzh.ifi.seal.soprafs16.utils.InputArgValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class GameCommandController
        extends GenericController {

    private static final Logger logger = LoggerFactory.getLogger(GameCommandController.class);

    @Autowired
    private GameService gameService;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private GameRepository gameRepo;

    private static final String CONTEXT = "/games";

    @RequestMapping(
            value = CONTEXT,
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Game createGame(@RequestBody Game game, @RequestParam("token") String userToken) {
        logger.debug("POST:{} - token: {}, {}", CONTEXT, userToken, game.toString() );

        User tokenOwner = InputArgValidator.checkTokenHasValidUser(userToken, userRepo, "token");
        return gameService.createGame(game, tokenOwner, game.getNumberOfPlayers());
    }

    @RequestMapping(value = CONTEXT + "/{gameId}/start", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void startGame(@PathVariable Long gameId, @RequestParam("token") String userToken) {
        logger.debug("POST:{} - token: {}, gameid: {}",  CONTEXT + "/{gameId}/start", userToken, gameId );

        gameService.startGame(gameId, userToken);
    }


    @RequestMapping(value = CONTEXT + "/{gameId}/stop", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void stopGame(@PathVariable Long gameId, @RequestParam("token") String userToken) {
        logger.debug("stopGame: " + gameId);

        Game game = gameRepo.findOne(gameId);
        User owner = userRepo.findByToken(userToken);

        if (owner != null && game != null && game.getOwner().equals(owner.getUsername())) {
            gameRepo.delete(game);
        }
    }

}