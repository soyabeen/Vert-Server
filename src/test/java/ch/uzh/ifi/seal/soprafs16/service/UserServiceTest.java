package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.exception.InvalidInputException;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
import ch.uzh.ifi.seal.soprafs16.utils.TokenGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by soyabeen on 31.03.16.
 */
public class UserServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceTest.class);

    @Mock
    private PlayerRepository playerRepo;

    @Mock
    private TokenGenerator token;

    @InjectMocks
    private PlayerService playerService;

    private Player expectedPlayer;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        String token = "token string";

        Player player = new Player("Bicyclerepairman");
        player.setToken(token);


        expectedPlayer = new Player("Bicyclerepairman");
        expectedPlayer.setId(1L);
        expectedPlayer.setToken(token);

        when(playerRepo.save(any(Player.class))).thenReturn(expectedPlayer);
        when(playerRepo.findByUsername("userNameAlreadyInUse")).thenReturn(expectedPlayer);
    }

    @Test
    public void createNewUserSuccessfully() {
        Player result = playerService.createPlayer(new Player("Bicyclerepairman"));

        Assert.assertFalse("User must not be null", result == null);
        Assert.assertThat(result.getId(), is(expectedPlayer.getId()));
        Assert.assertThat(result.getUsername(), is(expectedPlayer.getUsername()));
        Assert.assertThat(result.getToken(), is(expectedPlayer.getToken()));
    }

    @Test
    public void failUserCreationWithInvalidUserNameArg() {
        try {
            playerService.createPlayer(new Player());
            Assert.fail("Missing (null) username should throw InvalidInputException");
        } catch (InvalidInputException e) {
            Assert.assertTrue("Message must contain 'username'.", e.getMessage().contains("username"));
        }

        try {
            playerService.createPlayer(new Player(""));
            Assert.fail("Missing (empty) username should throw InvalidInputException");
        } catch (InvalidInputException e) {
            Assert.assertTrue("Message must contain 'username'.", e.getMessage().contains("username"));
        }

        try {
            playerService.createPlayer(new Player("userNameAlreadyInUse"));
            Assert.fail("Occupied username should throw InvalidInputException");
        } catch (InvalidInputException e) {
            Assert.assertTrue("Message must contain 'is already in use'.", e.getMessage().contains("is already in use"));
        }
    }

}
