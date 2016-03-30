package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.Application;
import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.UserRepository;
import ch.uzh.ifi.seal.soprafs16.utility.GameBuilder;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({ "server.port=0" })
public class CharacterQueryControllerIntegrationTest {

    private static final Logger logger  = LoggerFactory.getLogger(CharacterQueryControllerIntegrationTest.class);

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

    @Before
    public void setUp() throws MalformedURLException {
        this.base = new URL("http://localhost:" + port + "/");
        this.template = new TestRestTemplate();

        gameRepo.deleteAll();
        userRepo.deleteAll();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCharacters() {
        // create empty game
        Game game = gameBuilder.init("Game1", "Owner1").build();

        // check that all characters are available after game init
        String context = base + "games/" + game.getId() + "/characters";
        List<Character> charsBefore = template.getForObject(context, List.class);
        Assert.assertEquals(7, charsBefore.size());

        // add players to game
        gameBuilder.addRandomUserAndPlayer(Character.GHOST).addRandomUserAndPlayer(Character.BELLE).build();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(context)
                .queryParam("filter", "AVAILABLE");

        List<Character> charsAfter = template.getForObject(builder.build().encode().toUri(), List.class);

        Assert.assertThat(charsAfter.size(), is(5));
        Assert.assertTrue(!charsAfter.contains(Character.GHOST));
        Assert.assertTrue(!charsAfter.contains(Character.BELLE));
    }
}
