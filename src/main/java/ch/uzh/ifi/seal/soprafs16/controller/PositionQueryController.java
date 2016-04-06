package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import ch.uzh.ifi.seal.soprafs16.service.PositionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexanderhofmann on 26/03/16.
 */
@RestController
public class PositionQueryController extends GenericController {
    private static final Logger logger = LoggerFactory.getLogger(PositionQueryController.class);

    @Autowired
    private PositionService positionService;

    private static final String CONTEXT = "/games/{gameId}/positions";

    @RequestMapping(value = CONTEXT, method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Positionable> getPositionables(@PathVariable Long gameId, @RequestParam(value="type", required=false) String filter) {
        logger.debug("getPositionables");
        return positionService.listPositionablesForGame(gameId);
    }

}
