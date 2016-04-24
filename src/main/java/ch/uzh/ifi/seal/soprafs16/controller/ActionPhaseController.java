package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.constant.Turn;
import ch.uzh.ifi.seal.soprafs16.dto.TurnDTO;
import ch.uzh.ifi.seal.soprafs16.model.Move;
import ch.uzh.ifi.seal.soprafs16.model.Round;
import ch.uzh.ifi.seal.soprafs16.service.PhaseLogicService;
import ch.uzh.ifi.seal.soprafs16.service.RoundService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by mirkorichter on 24.04.16.
 */
public class ActionPhaseController extends GenericController {

    private static final Logger logger = LoggerFactory.getLogger(ActionPhaseController.class);

    @Autowired
    private PhaseLogicService phaseLogicService;

    private final String CONTEXT = "/games/{gameId}/rounds/{nthRound}/turns";

    @RequestMapping(value = CONTEXT + "/{nthTurn}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public TurnDTO getRoundById(@PathVariable Long gameId, @PathVariable Integer nthRound, @PathVariable Integer nthTurn) {
        //TODO: check if request of possibilities is legal (its player's turn)
        return phaseLogicService.getPossibilities();
    }


}
