package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.Application;
import ch.uzh.ifi.seal.soprafs16.helper.GameBuilder;
import ch.uzh.ifi.seal.soprafs16.helper.PlayerBuilder;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
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
@IntegrationTest({"server.port=0"})
public class GameQueryControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(GameQueryControllerTest.class);

    @Value("${local.server.port}")
    private int port;

    private URL base;
    private RestTemplate template;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private GameBuilder gameBuilder;

    @Autowired
    private PlayerBuilder playerBuilder;

    @Before
    public void setUp() throws MalformedURLException {
        this.base = new URL("http://localhost:" + port + "/games");
        this.template = new TestRestTemplate();

        gameRepo.deleteAll();
        userRepo.deleteAll();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void listGamesTest() {
        // No game exist yet
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(base.toString());

        ResponseEntity<List> emptyResponse = template.getForEntity(uriBuilder.build().encode().toUri().toString(), List.class);
        Assert.assertTrue(emptyResponse.getBody().isEmpty());

        // Games exist
        Game game1 = gameBuilder.init("listGamesTest1", "listGamesTest1").build();
        Game game2 = gameBuilder.init("listGamesTest2", "listGamesTest2").build();

        uriBuilder = UriComponentsBuilder.fromHttpUrl(base.toString());

        ResponseEntity<Game[]> response = template.getForEntity(uriBuilder.build().encode().toUri().toString(), Game[].class);
        Game[] result = response.getBody();

        Assert.assertThat(game1.getId(), is(result[0].getId()));
        Assert.assertThat(game1.getName(), is(result[0].getName()));
        Assert.assertThat(game1.getOwner(), is(result[0].getOwner()));

        Assert.assertThat(game2.getId(), is(result[1].getId()));
        Assert.assertThat(game2.getName(), is(result[1].getName()));
        Assert.assertThat(game2.getOwner(), is(result[1].getOwner()));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getGameTest() {
        // test valid query
        Game game3 = gameBuilder.init("listGamesTest3", "listGamesTest3").build();
        Long gameId = game3.getId();

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(base.toString() + "/" + gameId);
        ResponseEntity<Game> response = template.getForEntity(uriBuilder.build().encode().toUri().toString(), Game.class);
        Game result = response.getBody();

        Assert.assertThat(game3.getId(), is(result.getId()));
        Assert.assertThat(game3.getName(), is(result.getName()));
        Assert.assertThat(game3.getOwner(), is(result.getOwner()));

        // invalid query
        uriBuilder = UriComponentsBuilder.fromHttpUrl(base.toString() + "/-1");
        response = template.getForEntity(uriBuilder.build().encode().toUri().toString(), Game.class);
        result = response.getBody();

        Assert.assertNull(result);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getPlayerTest() {
        // test valid query
        Player player =  playerBuilder.getRandomPlayer();
        Game game4 = gameBuilder.init("listGamesTest4", "listGamesTest4").addUser(player).build();
        Long gameId = game4.getId();
        Long userId = player.getId();

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(
                base.toString() + "/" + gameId + "/player/" + userId);

        logger.debug(uriBuilder.build().encode().toUri().toString());

        ResponseEntity<Player> response = template.getForEntity(uriBuilder.build().encode().toUri().toString(), Player.class);
        Player result = response.getBody();

        Assert.assertThat(player.getId(), is(result.getId()));
        Assert.assertThat(player.getUsername(), is(result.getUsername()));
        Assert.assertThat(player.getToken(), is(result.getToken()));

        // invalid query
        uriBuilder = UriComponentsBuilder.fromHttpUrl(base.toString() + "/" + gameId + "/player/-1");
        response = template.getForEntity(uriBuilder.build().encode().toUri().toString(), Player.class);
        result = response.getBody();
        Assert.assertNull(result);
    }
}
