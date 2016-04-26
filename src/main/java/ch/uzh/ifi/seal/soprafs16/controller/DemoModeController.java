package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.service.DemoModeService;
import ch.uzh.ifi.seal.soprafs16.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Used to initialise the demo game.
 * <p>
 * Created by soyabeen on 26.04.16.
 */
public class DemoModeController extends GenericController {

    private static final Logger logger = LoggerFactory.getLogger(DemoModeController.class);

    @Autowired
    private DemoModeService demoService;

    private static final String CONTEXT = "/demo";

    @RequestMapping(
            value = CONTEXT,
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Game createDemoGame() {
        logger.debug("POST:{} - createDemoGame");

        return demoService.initDemoGame();
    }
}
