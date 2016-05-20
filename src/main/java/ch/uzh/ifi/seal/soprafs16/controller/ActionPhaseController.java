package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs16.dto.TurnDTO;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
import ch.uzh.ifi.seal.soprafs16.service.ActionPhaseService;
import ch.uzh.ifi.seal.soprafs16.utils.InputArgValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


/**
 * Created by mirkorichter on 24.04.16.
 */
@RestController
public class ActionPhaseController extends GenericController {

    private final String CONTEXT = "/games/{gameId}/actions";

    @Autowired
    private ActionPhaseService actionService;

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private GameRepository gameRepo;

    @RequestMapping(value = CONTEXT, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public TurnDTO getPossibilities(@PathVariable Long gameId,
                                    @RequestParam("token") String userToken) {

        Player tokenOwner = InputArgValidator.checkTokenHasValidPlayer(userToken, playerRepo, "token");
        Game game = (Game) InputArgValidator.checkAvailabeId(gameId, gameRepo, "gameid");
        InputArgValidator.checkItIsPlayersTurn(tokenOwner, game);
        InputArgValidator.checkGameState(game, GameStatus.ACTIONPHASE);
        return actionService.sendPossibilities(gameId);
    }

    @RequestMapping(value = CONTEXT, method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void chosenPossibility(@PathVariable Long gameId,
                                  @RequestParam("token") String userToken,
                                  @RequestBody TurnDTO turnDTO) {

        Player tokenOwner = InputArgValidator.checkTokenHasValidPlayer(userToken, playerRepo, "token");
        Game game = (Game) InputArgValidator.checkAvailabeId(gameId, gameRepo, "gameid");
        InputArgValidator.checkItIsPlayersTurn(tokenOwner, game);
        InputArgValidator.checkGameState(game, GameStatus.ACTIONPHASE);

        actionService.executeDTO(gameId, turnDTO);
    }


}