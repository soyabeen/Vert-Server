package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.Application;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.hamcrest.core.Is.is;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({ "server.port=0" })
public class UserQueryControllerIntegrationTest {

    private static final Logger logger  = LoggerFactory.getLogger(UserQueryControllerIntegrationTest.class);

    @Value("${local.server.port}")
    private int          port;

    private URL          base;
    private RestTemplate template;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private UserBuilder userBuilder;

    @Before
    public void setUp()
            throws MalformedURLException {
        this.base = new URL("http://localhost:" + port + "/");
        this.template = new TestRestTemplate();

        gameRepo.deleteAll();
        userRepo.deleteAll();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCreateUserSuccess() {
        List<User> usersBefore = template.getForObject(base + "/users", List.class);
        Assert.assertEquals(0, usersBefore.size());

        User user1 = userBuilder.getRandomUser();
        User user2 = userBuilder.getRandomUser();

        User[] usersAfter = template.getForObject(base + "/users", User[].class);
        Assert.assertThat(usersAfter.length, is(2));

        Assert.assertThat(usersAfter[0].getId(), is(user1.getId()));
        Assert.assertThat(usersAfter[0].getToken(), is(user1.getToken()));

        Assert.assertThat(usersAfter[1].getId(), is(user2.getId()));
        Assert.assertThat(usersAfter[1].getToken(), is(user2.getToken()));
    }
}