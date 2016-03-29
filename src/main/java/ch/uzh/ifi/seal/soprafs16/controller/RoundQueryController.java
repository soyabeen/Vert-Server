package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.model.Round;
import ch.uzh.ifi.seal.soprafs16.service.RoundService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by soyabeen on 26.03.16.
 */
@RestController
public class RoundQueryController extends GenericController {

    private static final Logger logger = LoggerFactory.getLogger(RoundQueryController.class);

    private final String CONTEXT = "/games/{gameId}/rounds";

    @Autowired
    private RoundService roundService;

    @RequestMapping(value = CONTEXT + "{roundId}")
    @ResponseStatus(HttpStatus.OK)
    public Round getRoundById(@PathVariable Long gameId, @PathVariable Long roundId) {
        return null;
    }
}
