package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(UserQueryController.CONTEXT)
public class UserQueryController
        extends GenericController {

    private static final Logger logger  = LoggerFactory.getLogger(UserQueryController.class);

    static final String CONTEXT = "/users";

    @Autowired
    private UserRepository userRepo;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<User> listUsers() {
        logger.debug("listUsers");

        List<User> result = new ArrayList<>();
        userRepo.findAll().forEach(result::add);

        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable Long userId) {
        logger.debug("getUser: " + userId);

        return userRepo.findOne(userId);
    }
}
