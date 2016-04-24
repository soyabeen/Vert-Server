package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.dto.TurnDTO;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
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
public class ActionPhaseController extends GenericController {

    private static final Logger logger = LoggerFactory.getLogger(ActionPhaseController.class);

    @Autowired
    private PhaseLogicService phaseLogicService;

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private GameRepository gameRepo;

    private final String CONTEXT = "/games/{gameId}/rounds/{nthRound}/turns";

    @RequestMapping(value = CONTEXT + "/{nthTurn}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public TurnDTO getPossibilities(@PathVariable Long gameId, @PathVariable Integer nthRound,
                                @PathVariable Integer nthTurn, @RequestParam("token") String userToken) {

        Player tokenOwner = InputArgValidator.checkTokenHasValidPlayer(userToken, playerRepo, "token");
        Game game = gameRepo.findOne(gameId);
        InputArgValidator.checkItIsPlayersTurn(tokenOwner,game);
        return phaseLogicService.getPossibilities();
    }

    @RequestMapping(value = CONTEXT + "/{nthTurn}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void chosenPossibility(@PathVariable Long gameId, @PathVariable Integer nthRound,
                                    @PathVariable Integer nthTurn, @RequestBody TurnDTO turnDTO) {

        //TODO: Check if DTO is valid
        //TODO: Use DTO to update game
        throw  new IllegalStateException("Not yet implemented");
    }


}
