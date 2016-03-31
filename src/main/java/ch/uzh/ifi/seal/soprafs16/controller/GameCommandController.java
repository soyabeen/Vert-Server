package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Move;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.MoveRepository;
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
    private GameService gameSerivce;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private GameRepository gameRepo;
    @Autowired
    private MoveRepository moveRepo;

    private static final String CONTEXT = "/games";

    @RequestMapping(
            value = CONTEXT,
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Game addGame(@RequestBody Game game, @RequestParam("token") String userToken) {

        logger.debug("POST:{} - token: {}, {}", CONTEXT, userToken, game.toString() );

        User tokenOwner = InputArgValidator.checkTokenHasValidUser(userToken, userRepo, "token");
        return gameSerivce.createGame(game.getName(), game.getOwner(), game.getNumberOfPlayers());
    }

    @RequestMapping(value = CONTEXT + "/{gameId}/start", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void startGame(@PathVariable Long gameId, @RequestParam("token") String userToken) {
        logger.debug("startGame: " + gameId);

        Game game = gameRepo.findOne(gameId);
        User owner = userRepo.findByToken(userToken);

        if (owner != null && game != null && game.getOwner().equals(owner.getUsername())) {
            // TODO: Start game
        }
    }

    @RequestMapping(value = CONTEXT + "/{gameId}/stop", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void stopGame(@PathVariable Long gameId, @RequestParam("token") String userToken) {
        logger.debug("stopGame: " + gameId);

        Game game = gameRepo.findOne(gameId);
        User owner = userRepo.findByToken(userToken);

        if (owner != null && game != null && game.getOwner().equals(owner.getUsername())) {
            // TODO: Stop game
        }
    }

    @RequestMapping(value = CONTEXT + "/{gameId}/move", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Move addMove(@RequestBody Move move, @PathVariable Long gameId) {
        logger.debug("addMove: " + move);
        // TODO Mapping into Move + execution of move

        move.setGame(gameRepo.findOne(gameId));
        move = moveRepo.save(move);

        return move;
    }
}