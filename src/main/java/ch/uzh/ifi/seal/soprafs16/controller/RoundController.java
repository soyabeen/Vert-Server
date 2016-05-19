package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.constant.Turn;
import ch.uzh.ifi.seal.soprafs16.dto.TurnDTO;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Move;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Round;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
import ch.uzh.ifi.seal.soprafs16.service.RoundService;
import ch.uzh.ifi.seal.soprafs16.utils.InputArgValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created by soyabeen on 26.03.16.
 */
@RestController
public class RoundController extends GenericController {

    private static final Logger logger = LoggerFactory.getLogger(RoundController.class);

    @Autowired
    private RoundService roundService;

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private PlayerRepository playerRepo;

    private final String CONTEXT = "/games/{gameId}/rounds";

    @RequestMapping(value = CONTEXT, method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Round getCurrentRoundInformation(@PathVariable Long gameId) {
        logger.debug("GET:{} - Args. gameId <{}>.", CONTEXT, gameId);
        return roundService.getCurrentRoundInformation(gameId);
    }

    @RequestMapping(value = CONTEXT + "/{nthRound}/turns", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String makeMove(@PathVariable Long gameId, @PathVariable Integer nthRound, @RequestParam String token,
                           @RequestBody TurnDTO turnDTO) {

        Player tokenOwner = InputArgValidator.checkTokenHasValidPlayer(token, playerRepo, "token");
        Game game = (Game) InputArgValidator.checkAvailabeId(gameId, gameRepo, "gameid");
        InputArgValidator.checkItIsPlayersTurn(tokenOwner,game);
        Move move = roundService.getMoveFromDTO(gameId, token, turnDTO);
        return roundService.makeAMove(gameId, nthRound, move);
    }

    @RequestMapping(value = CONTEXT + "/{nthRound}/turns", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Turn> listTurnsForRound(@PathVariable Long gameId, @PathVariable Integer nthRound) {
        return roundService.listTurnsForRound(gameId, nthRound);
    }

}
