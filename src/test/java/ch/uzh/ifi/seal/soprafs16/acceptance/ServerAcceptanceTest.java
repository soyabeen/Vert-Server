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

import java.net.URI;
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
    private void setUp() {
        temp = new RestTemplate();
        base = new URL("http://localhost:" + port );
    }


    private Player createPlayer(String name) {
        Player ownerShell = new Player("player-1");
        Player owner = temp.postForObject(createUserUri, ownerShell, Player.class);

        Assert.assertNotNull("Created owner is not null.", owner);
        Assert.assertNotNull("Owner id not null.", owner.getId());
        Assert.assertNotNull("Owner token not null.", owner.getToken());
        Assert.assertEquals("Owner name is player-1.", "player-1", owner.getUsername());

    }

    @Test
    public void runAcceptanceTest() throws URISyntaxException {

        URI createUserUri = new URI("http://localhost:" + port + "/users");
        Player ownerShell = new Player("player-1");
        Player owner = temp.postForObject(createUserUri, ownerShell, Player.class);

        Assert.assertNotNull("Created owner is not null.", owner);
        Assert.assertNotNull("Owner id not null.", owner.getId());
        Assert.assertNotNull("Owner token not null.", owner.getToken());
        Assert.assertEquals("Owner name is player-1.", "player-1", owner.getUsername());

        logger.debug(owner.toString());


    }

}
