package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.dto.TurnDTO;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
import ch.uzh.ifi.seal.soprafs16.service.ActionPhaseService;
import ch.uzh.ifi.seal.soprafs16.service.PhaseLogicService;
import ch.uzh.ifi.seal.soprafs16.utils.InputArgValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


/**
 * Created by mirkorichter on 24.04.16.
 */
@RestController
public class ActionPhaseController extends GenericController {

    private static final Logger logger = LoggerFactory.getLogger(ActionPhaseController.class);

    @Autowired
    private ActionPhaseService actionService;

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private GameRepository gameRepo;

    private final String CONTEXT = "/games/{gameId}/actions";

    @RequestMapping(value = CONTEXT, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public TurnDTO getPossibilities(@PathVariable Long gameId,
                                    @RequestParam("token") String userToken) {

        Player tokenOwner = InputArgValidator.checkTokenHasValidPlayer(userToken, playerRepo, "token");
        Game game = gameRepo.findOne(gameId);
        InputArgValidator.checkItIsPlayersTurn(tokenOwner,game);
        return actionService.sendPossibilities(gameId);
    }

    @RequestMapping(value = CONTEXT, method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void chosenPossibility(@PathVariable Long gameId,
                                  @RequestParam("token") String userToken,
                                  @RequestBody TurnDTO turnDTO) {

        Player tokenOwner = InputArgValidator.checkTokenHasValidPlayer(userToken, playerRepo, "token");
        Game game = gameRepo.findOne(gameId);
        InputArgValidator.checkItIsPlayersTurn(tokenOwner,game);
        //TODO: Check if DTO is valid if necessary
        actionService.executeDTO(gameId, turnDTO);
    }


}