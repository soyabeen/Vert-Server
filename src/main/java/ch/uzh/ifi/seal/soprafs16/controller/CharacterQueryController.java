package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.service.CharacterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class CharacterQueryController extends GenericController {

    private static final Logger logger = LoggerFactory.getLogger(CharacterQueryController.class);

    private static final String CONTEXT = "/games/{gameId}/characters";

    @Autowired
    public CharacterService charService;

    @RequestMapping(value = CONTEXT, method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Character> getCharacters(@PathVariable Long gameId, @RequestParam(value = "filter", required = false) String filter) {
        logger.debug("Call getCharacters for gameId {} and filter {}", gameId, filter);
        if ("AVAILABLE".equals(filter)) {
            return charService.listAvailableCharactersByGame(gameId);
        }
        return charService.listCharacters();
    }
}