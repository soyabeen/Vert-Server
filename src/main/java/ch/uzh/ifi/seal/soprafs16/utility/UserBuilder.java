package ch.uzh.ifi.seal.soprafs16.utility;

import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.User;

import java.util.UUID;

/**
 * Created by alexanderhofmann on 29/03/16.
 */
public class UserBuilder {

    private UserBuilder() {
    }

    /**
     * Generates a random user.
     * Name, owner and token are all generated randomly.
     * @return random user
     */
    public static User getRandomUser() {
        User user = new User(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        user.setToken(UUID.randomUUID().toString());
        user.setStatus(UserStatus.ONLINE);
        return user;
    }

    public static User getRandomUserWithPlayer(Character character) {
        User user = getRandomUser();

        Player player = new Player();
        player.setCharacter(character);
        user.setPlayer(player);

        return user;
    }
}
