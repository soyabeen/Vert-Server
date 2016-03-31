package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.repositories.UserRepository;
import ch.uzh.ifi.seal.soprafs16.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
public class UserCommandController
        extends GenericController {

    private static final Logger logger = LoggerFactory.getLogger(UserCommandController.class);

    private static final String CONTEXT = "/users";

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepo;

    @RequestMapping(value = CONTEXT, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public User addUser(@RequestBody User user) {
        logger.debug("Call addUser for name <{}>, username <{}>.", user.getName(), user.getUsername());
        return userService.createUser(user.getName(), user.getUsername());
    }

    @RequestMapping(method = RequestMethod.POST, value = "{userId}/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@PathVariable Long userId, @RequestParam("token") String userToken) {
        logger.debug("getUser: " + userId);

        User user = userRepo.findOne(userId);

        if (user != null && user.getToken().equals(userToken)) {
            user.setStatus(UserStatus.OFFLINE);
            userRepo.save(user);
        }
    }
}
