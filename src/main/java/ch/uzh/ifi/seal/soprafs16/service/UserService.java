package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by soyabeen on 31.03.16.
 */
@Service("userService")
public class UserService {

    @Autowired
    private UserRepository userRepo;

    public User addNewUser(String name, String username) {

        User user = new User(name, username);
        user.setStatus(UserStatus.ONLINE);
        user.setToken(UUID.randomUUID().toString());
        return userRepo.save(user);
    }
}
