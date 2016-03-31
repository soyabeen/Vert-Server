package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs16.exception.InvalidInputException;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.repositories.UserRepository;
import ch.uzh.ifi.seal.soprafs16.utils.InputArgValidator;
import ch.uzh.ifi.seal.soprafs16.utils.TokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by soyabeen on 31.03.16.
 */
@Service("userService")
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(RoundService.class);

    @Autowired
    private UserRepository userRepo;

    private TokenGenerator generator;

    public UserService() {
        generator = new TokenGenerator();
    }

    public User createUser(String name, String username) {

        InputArgValidator.checkNotEmpty(name, "name");
        InputArgValidator.checkNotEmpty(username, "username");
        InputArgValidator.checkUserNameNotUsed(username, userRepo, "username");

        User user = new User(name, username);
        user.setStatus(UserStatus.ONLINE);
        user.setToken(generator.generateToken());
        return userRepo.save(user);
    }
}
