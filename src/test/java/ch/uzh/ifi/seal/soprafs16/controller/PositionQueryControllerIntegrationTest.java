package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.Application;
import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import ch.uzh.ifi.seal.soprafs16.utility.GameBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({ "server.port=0" })
public class PositionQueryControllerIntegrationTest {

    @SuppressWarnings("unused")
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(PositionQueryControllerIntegrationTest.class);

    @Value("${local.server.port}")
    private int port;
    private URL base;

    private RestTemplate template;

    @Autowired
    private GameBuilder gameBuilder;

    @Before
    public void setUp()
            throws MalformedURLException {
        this.base = new URL("http://localhost:" + port + "/");
        this.template = new TestRestTemplate();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetPositionablesSuccess() throws URISyntaxException {
        // create empty game
        Game game = gameBuilder.init("Game1", "Owner1").build();

        URI posURI = new URI(base + "games/" + game.getId() + "/positions");

        List<Positionable> positionablesBefore = template.getForObject(posURI, List.class);
        Assert.assertEquals(0, positionablesBefore.size());

        // add one positionable
        gameBuilder.addRandomUserAndPlayer(Character.BELLE).build();

        List<Positionable> positionableAfter = template.getForObject(posURI, List.class);
        Assert.assertEquals(1, positionableAfter.size());

        // add a second positionable
        gameBuilder.addRandomLoot().build();

        positionableAfter = template.getForObject(posURI, List.class);
        Assert.assertEquals(2, positionableAfter.size());
    }
}
