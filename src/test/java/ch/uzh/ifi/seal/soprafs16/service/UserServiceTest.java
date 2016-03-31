package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs16.exception.InvalidInputException;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.repositories.UserRepository;
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
    private UserRepository userRepository;

    @Mock
    private TokenGenerator token;

    @InjectMocks
    private UserService userService;

    private User expectedUser;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        User userShell = new User("Tom Turbo", "Bicyclerepairman");
        userShell.setStatus(UserStatus.ONLINE);
        userShell.setToken("token string");

        expectedUser = new User("Tom Turbo", "Bicyclerepairman");
        expectedUser.setId(1L);
        expectedUser.setStatus(UserStatus.ONLINE);
        expectedUser.setToken("token string");

        when(token.generateToken()).thenReturn("token string");
        when(userRepository.save(any(User.class))).thenReturn(expectedUser);
        when(userRepository.findByUsername("userNameAlreadyInUse")).thenReturn(expectedUser);
    }

    @Test
    public void createNewUserSuccessfully() {
        User result = userService.createUser("Tom Turbo", "Bicyclerepairman");

        Assert.assertFalse("User must not be null", result == null);
        Assert.assertThat(result.getId(), is(expectedUser.getId()));
        Assert.assertThat(result.getName(), is(expectedUser.getName()));
        Assert.assertThat(result.getUsername(), is(expectedUser.getUsername()));
        Assert.assertThat(result.getStatus(), is(expectedUser.getStatus()));
        Assert.assertThat(result.getToken(), is(expectedUser.getToken()));
    }

    @Test
    public void failUserCreationWithInvalidNameArg() {

        try {
            userService.createUser(null, "userName");
            Assert.fail("Missing (null) username should throw InvalidInputException");
        } catch (InvalidInputException e) {
            Assert.assertTrue("Message must contain 'name'.", e.getMessage().contains("name"));
        }

        try {
            userService.createUser("", "userName");
            Assert.fail("Missing (empty) username should throw InvalidInputException");
        } catch (InvalidInputException e) {
            Assert.assertTrue("Message must contain 'name'.", e.getMessage().contains("name"));
        }

    }

    @Test
    public void failUserCreationWithInvalidUserNameArg() {
        try {
            userService.createUser("name", null);
            Assert.fail("Missing (null) username should throw InvalidInputException");
        } catch (InvalidInputException e) {
            Assert.assertTrue("Message must contain 'username'.", e.getMessage().contains("username"));
        }

        try {
            userService.createUser("name", "");
            Assert.fail("Missing (empty) username should throw InvalidInputException");
        } catch (InvalidInputException e) {
            Assert.assertTrue("Message must contain 'username'.", e.getMessage().contains("username"));
        }

        try {
            userService.createUser("name", "userNameAlreadyInUse");
            Assert.fail("Occupied username should throw InvalidInputException");
        } catch (InvalidInputException e) {
            Assert.assertTrue("Message must contain 'is already in use'.", e.getMessage().contains("is already in use"));
        }
    }

}
