package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.Application;
import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.exception.InvalidInputException;
import ch.uzh.ifi.seal.soprafs16.helper.GameBuilder;
import ch.uzh.ifi.seal.soprafs16.helper.UserBuilder;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.User;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({ "server.port=0" })
public class GameCommandControllerTest {

    private static final Logger logger  = LoggerFactory.getLogger(GameCommandControllerTest.class);

    @Value("${local.server.port}")
    private int          port;

    private URL          base;
    private RestTemplate template;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private GameBuilder gameBuilder;

    @Autowired
    private UserBuilder userBuilder;

    @Before
    public void setUp() throws MalformedURLException {
        this.base = new URL("http://localhost:" + port + "/games");
        this.template = new TestRestTemplate();

        gameRepo.deleteAll();
        userRepo.deleteAll();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void addGameTest() {
        // test valid query
        User user = userBuilder.getRandomUser();
        Game game = gameBuilder.initNoPersistence("addGameTest", user.getUsername()).build();

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(base.toString())
                .queryParam("token", user.getToken());

        HttpEntity<Game> gameEntity = new HttpEntity<>(game);

        ResponseEntity<Game> response = template.exchange(uriBuilder.build().encode().toUri(),
                HttpMethod.POST,
                gameEntity,
                Game.class);


        Game result = gameRepo.findOne(response.getBody().getId());
        Assert.assertThat(result.getName(), is(game.getName()));
        Assert.assertThat(result.getOwner(), is(game.getOwner()));

        //test invalid query
        uriBuilder = UriComponentsBuilder.fromHttpUrl(base.toString())
                .queryParam("token", "a");

        gameEntity = new HttpEntity<>(game);


        try {
            response = template.exchange(uriBuilder.build().encode().toUri(),
                    HttpMethod.POST,
                    gameEntity,
                    Game.class);
            //Should throw an exception
            Assert.fail();
        } catch (Exception e) {
        }
    }
}
