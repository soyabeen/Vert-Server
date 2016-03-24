package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.GameConstants;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Move;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.MoveRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
public class GameCharacterServiceController
        extends GenericService {

    Logger                 logger  = LoggerFactory.getLogger(GameCharacterServiceController.class);


    private final String   CONTEXT = "/games/{gameId}/characters";


    @RequestMapping(value = CONTEXT)
    @ResponseStatus(HttpStatus.OK)
    public String getCharacters(@PathVariable Long gameId, @RequestParam(value="filter", required=false) String filter) {
        if ("AVAILABLE".equals(filter)) {
            return gameId + " BELLE\n";
        }
        return gameId + " BELLE\n" + gameId + " DOC";
    }


}