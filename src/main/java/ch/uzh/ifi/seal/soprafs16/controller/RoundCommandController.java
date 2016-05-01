package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.dto.TurnDTO;
import ch.uzh.ifi.seal.soprafs16.model.Move;
import ch.uzh.ifi.seal.soprafs16.service.RoundService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by antoio on 5/1/16.
 */
@RestController
public class RoundCommandController extends GenericController {

    private static final Logger logger = LoggerFactory.getLogger(RoundCommandController.class);

    @Autowired
    private RoundService roundService;


    private final String CONTEXT = "/games/{gameId}/rounds";

    @RequestMapping(value = CONTEXT + "/{nthRound}/turns", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String makeAMove(@PathVariable Long gameId, @PathVariable Integer nthRound, @RequestParam String token,
                            @RequestBody TurnDTO turnDTO) {
        Move move = roundService.getMoveFromDTO(gameId, token, turnDTO);
        return roundService.makeAMove(gameId, nthRound, move);
    }
}
