package ch.uzh.ifi.seal.soprafs16.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class UserQueryController
        extends GenericController {

    private static final Logger logger = LoggerFactory.getLogger(UserQueryController.class);

    static final String CONTEXT = "/users";

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET, value = CONTEXT)
    @ResponseStatus(HttpStatus.OK)
    public List<User> listUsers() {
        logger.debug("listUsers");
        return userService.listUsers();
    }

    @RequestMapping(method = RequestMethod.GET, value = CONTEXT + "/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable Long userId) {
        logger.debug("getPlayer: " + userId);

        return userService.getUser(userId);
    }
}
