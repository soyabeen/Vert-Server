package ch.uzh.ifi.seal.soprafs16.helper;

import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by alexanderhofmann on 29/03/16.
 */
@Service
public class UserBuilder {


    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PlayerRepository playerRepo;

    /**
     * Generates a random user.
     * Name, owner and token are all generated randomly.
     * @return random user
     */
    public User getRandomUser() {
        User user = new User(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        user.setToken(UUID.randomUUID().toString());
        user.setStatus(UserStatus.ONLINE);
        return userRepo.save(user);
    }

    /**
     * Generates a random user and player with a given character.
     * @param character
     * @return
     */
    public User getRandomUserWithPlayer(Character character) {
        User user = getRandomUser();

        Player player = new Player();
        player.setCharacter(character);
        player = playerRepo.save(player);
        user.setPlayer(player);

        return userRepo.save(user);
    }
}
