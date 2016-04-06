package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Command Controller for the {@code/users} endpoint.
 */
@RestController
public class UserCommandController
        extends GenericController {

    private static final Logger logger = LoggerFactory.getLogger(UserCommandController.class);

    private static final String CONTEXT = "/users";

    @Autowired
    private UserService userService;

    /**
     * Adds a new user to the database.
     * @param user
     * @return
     */
    @RequestMapping(value = CONTEXT, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public User addUser(@RequestBody User user) {
        logger.debug("POST: - Args. name <{}>, username <{}>.", CONTEXT, user.getName(), user.getUsername());
        return userService.createUser(user.getName(), user.getUsername());
    }
}
