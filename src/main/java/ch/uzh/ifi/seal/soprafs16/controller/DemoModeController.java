package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.service.DemoModeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Used to initialise the demo game.
 * <p>
 * Created by soyabeen on 26.04.16.
 */
@RestController
public class DemoModeController extends GenericController {

    private static final Logger logger = LoggerFactory.getLogger(DemoModeController.class);

    @Autowired
    private DemoModeService demoService;

    @RequestMapping(
            value = "/demos",
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Game createDemoGame() {
        logger.debug("POST:{} - createDemoGame");
        return demoService.initDemoGame();
    }

    @RequestMapping(
            value = "/fastlanes",
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Game createFastLaneGame(@RequestBody Game game, @RequestParam("token") String userToken) {
        logger.debug("POST:{} - createFastLaneGame : game={}, token={}", game, userToken);
        return demoService.initFastLaneGame(game, userToken);
    }
}
