package ch.uzh.ifi.seal.soprafs16.acceptance;

import ch.uzh.ifi.seal.soprafs16.Application;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by soyabeen on 15.05.16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class ServerAcceptanceTest {

    private static final Logger logger = LoggerFactory.getLogger(ServerAcceptanceTest.class);

    @Value("${local.server.port}")
    private int port;
    private URL base;
    private RestTemplate temp;

    @Before
    public void setUp() throws MalformedURLException {
        temp = new RestTemplate();
        base = new URL("http://localhost:" + port);
    }


    private Player createPlayer(String name) {
        Player playerShell = new Player(name);
        Player player = temp.postForObject(base + "/users", playerShell, Player.class);

        Assert.assertNotNull("Created player is not null.", player);
        Assert.assertNotNull("Player id not null.", player.getId());
        Assert.assertNotNull("Player token not null.", player.getToken());
        Assert.assertEquals("Player name is " + name , name, player.getUsername());

        return player;
    }

    @Test
    public void runAcceptanceTest() throws URISyntaxException {

        Player owner = createPlayer("player-1");
        logger.debug(owner.toString());


    }

}
