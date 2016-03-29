package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.Application;
import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs16.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
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
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by antoio on 3/29/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class PlayerServiceIntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(CharacterServiceIntegrationTest.class);

    @Value("${local.server.port}")
    private int port;

    private URL base;
    private RestTemplate template;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PlayerRepository playerRepo;

    @Before
    public void setUp() throws MalformedURLException {
        this.base = new URL("http://localhost:" + port + "/");
        this.template = new TestRestTemplate();
    }

    @Test
    public void testCreatePlayerForUser() {
        Game game = new Game();
        game.setStatus(GameStatus.PENDING);
        game.setName("gameName");
        game.setOwner("gameOwner");
        game = gameRepo.save(game);

        User user1 = new User("duck", "donald");
        user1.setStatus(UserStatus.ONLINE);
        user1.setToken(UUID.randomUUID().toString());
        user1 = userRepo.save(user1);

        String context = base + "games/" + game.getId() + "/players";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(context)
                .queryParam("token", user1.getToken())
                .queryParam("character", Character.BELLE);

        String badResponse = "-1";
        String response = template.postForObject(builder.build().encode().toUri(), HttpMethod.POST, String.class);
        Assert.assertNotEquals(response, badResponse);

        // check that player was correctly created
        Player createdPlayer = playerRepo.findOne(Long.parseLong(response));
        Assert.assertNotNull(createdPlayer);
        Assert.assertThat(createdPlayer.getCharacter(), is(Character.BELLE));

        // check that player is assigned to user
        user1 = userRepo.findOne(user1.getId());
        Assert.assertNotNull(user1.getPlayer());
        Assert.assertEquals(user1.getPlayer().getId(), createdPlayer.getId());
        Assert.assertEquals(user1.getPlayer().getCharacter(), createdPlayer.getCharacter());

        // check that user was assigned to game
        game = gameRepo.findOne(game.getId());
        Assert.assertNotNull(game.getUsers());
        Assert.assertThat(game.getUsers().get(0).getId(), is(user1.getId()));
        Assert.assertThat(game.getUsers().get(0).getToken(), is(user1.getToken()));

        // check if it can choose same character as someone else
        User user2 = new User("Duck", "Avenger");
        user2.setStatus(UserStatus.ONLINE);
        user2.setToken(UUID.randomUUID().toString());
        user2 = userRepo.save(user2);

        builder = UriComponentsBuilder.fromHttpUrl(context)
                .queryParam("token", user2.getToken())
                .queryParam("character", Character.BELLE);

        response = template.postForObject(builder.build().encode().toUri(), HttpMethod.POST, String.class);
        Assert.assertThat(response, is(badResponse));

        // game doesn't exist
        builder = UriComponentsBuilder.fromHttpUrl(base + "games/2/players")
                .queryParam("token", user2.getToken())
                .queryParam("character", Character.BELLE);
        response = template.postForObject(builder.build().encode().toUri(), HttpMethod.POST, String.class);
        Assert.assertThat(response, is(badResponse));

        //TODO: Check if game is already full
    }
}
