package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.Application;
import ch.uzh.ifi.seal.soprafs16.constant.Character;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({ "server.port=0" })
public class GameCharacterServiceControllerIntegrationTest {

    private static final Logger logger  = LoggerFactory.getLogger(GameCharacterServiceControllerIntegrationTest.class);

    @Value("${local.server.port}")
    private int          port;

    private URL          base;
    private RestTemplate template;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private PlayerRepository playerRepo;

    @Before
    public void setUp()
            throws Exception {
        this.base = new URL("http://localhost:" + port + "/");
        this.template = new TestRestTemplate();

        gameRepo.deleteAll();
        userRepo.deleteAll();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCharacters() {
        Game game = new Game();
        game.setName("Game");
        game.setOwner("Owner");
        game = gameRepo.save(game);

        String context = base + "games/" + game.getId() + "/characters";

        List<Character> charsBefore = template.getForObject(context, List.class);
        Assert.assertEquals(7, charsBefore.size());

        User user = new User("Mike Meyers", "mm");
        user.setStatus(UserStatus.OFFLINE);
        user.setToken(UUID.randomUUID().toString());

        Player p1 = new Player();
        p1.setCharacter(Character.GHOST);
        p1 = playerRepo.save(p1);

        user.setPlayer(p1);
        user = userRepo.save(user);

        game.addUser(user);
        gameRepo.save(game);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(context)
                .queryParam("filter", "AVAILABLE");

        List<Character> charsAfter = template.getForObject(builder.build().encode().toUri(), List.class);

        Assert.assertThat(charsAfter.size(), is(6));
        Assert.assertTrue(!charsAfter.contains(Character.GHOST));
    }
}
