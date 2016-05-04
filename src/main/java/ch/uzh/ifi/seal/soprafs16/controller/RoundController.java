package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.constant.Turn;
import ch.uzh.ifi.seal.soprafs16.dto.TurnDTO;
import ch.uzh.ifi.seal.soprafs16.model.Move;
import ch.uzh.ifi.seal.soprafs16.model.Round;
import ch.uzh.ifi.seal.soprafs16.service.RoundService;
import org.hibernate.cfg.NotYetImplementedException;
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

    private final String CONTEXT = "/games/{gameId}/rounds";

    @RequestMapping(value = CONTEXT + "/{nthRound}/turns", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String makeMove(@PathVariable Long gameId, @PathVariable Integer nthRound, @RequestParam String token,
                           @RequestBody TurnDTO turnDTO) {
        Move move = roundService.getMoveFromDTO(gameId, token, turnDTO);
        return roundService.makeAMove(gameId, nthRound, move);
    }

    @ResponseStatus(HttpStatus.OK)
    public TurnDTO executeMove(@PathVariable Long gameId, @PathVariable Integer nthRound, @RequestParam String token,
                               @RequestBody TurnDTO turnDTO) {
        throw new NotYetImplementedException("Method is missing");
    }

    @RequestMapping(value = CONTEXT + "/{nthRound}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Round getRoundById(@PathVariable Long gameId, @PathVariable Integer nthRound) {
        return roundService.getRoundById(gameId, nthRound);
    }

    @RequestMapping(value = CONTEXT + "/{nthRound}/turns", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    // TODO: add parameter for filter options
    public List<Turn> listTurnsForRound(@PathVariable Long gameId, @PathVariable Integer nthRound) {
        return roundService.listTurnsForRound(gameId, nthRound);
    }

}
