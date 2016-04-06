package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.Application;
import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.helper.PlayerBuilder;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
import ch.uzh.ifi.seal.soprafs16.helper.GameBuilder;
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

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

/**
 * Created by antoio on 3/29/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class PlayerServiceIntegrationTest {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(CharacterServiceIntegrationTest.class);

    @Value("${local.server.port}")
    private int port;

    private URL base;
    private RestTemplate template;

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private GameBuilder gameBuilder;

    @Autowired
    private PlayerBuilder playerBuilder;

    private static final String BAD_RESPONSE= "-1";

    @Before
    public void setUp() throws MalformedURLException {
        this.base = new URL("http://localhost:" + port + "/");
        this.template = new TestRestTemplate();
    }

    @Test
    public void testCreatePlayerForUser() {
        Game game = gameBuilder.init("testCreatePlayerForUser", "testCreatePlayerForUser").build();
        String context = base + "games/" + game.getId() + "/players";

        Player player1 = playerBuilder.init().build();

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(context)
                .queryParam("token", player1.getToken())
                .queryParam("character", Character.BELLE);

        String response = template.postForObject(uriBuilder.build().encode().toUri(), HttpMethod.POST, String.class);
        Assert.assertNotEquals(response, BAD_RESPONSE);

        // check that player was correctly created
        Player createdPlayer = playerRepo.findOne(Long.parseLong(response));
        Assert.assertNotNull("Player1 not null.", createdPlayer);
        Assert.assertThat("Player1 has char BELLE.", createdPlayer.getCharacter(), is(Character.BELLE));

        // check that user was assigned to game
        game = gameRepo.findOne(game.getId());
        Assert.assertNotNull(game.getPlayers());
        Assert.assertThat(game.getPlayers().get(0).getId(), is(player1.getId()));
        Assert.assertThat(game.getPlayers().get(0).getToken(), is(player1.getToken()));

        // check if it can choose same character as someone else
        Player player2 = playerBuilder.init().build();

        uriBuilder = UriComponentsBuilder.fromHttpUrl(context)
                .queryParam("token", player2.getToken())
                .queryParam("character", Character.BELLE);

        response = template.postForObject(uriBuilder.build().encode().toUri(), HttpMethod.POST, String.class);
        Assert.assertThat(response, is(BAD_RESPONSE));

        // check if user has already chosen a player
        uriBuilder = UriComponentsBuilder.fromHttpUrl(context)
                .queryParam("token", player1.getToken())
                .queryParam("character", Character.DOC);
        response = template.postForObject(uriBuilder.build().encode().toUri(), HttpMethod.POST, String.class);

        Player createdPlayer2 = playerRepo.findOne(Long.parseLong(response));
        Assert.assertNotNull(createdPlayer2);
        Assert.assertThat(createdPlayer2.getCharacter(), is(Character.BELLE));

        // game doesn't exist
        uriBuilder = UriComponentsBuilder.fromHttpUrl(base + "games/10000000/players")
                .queryParam("token", player2.getToken())
                .queryParam("character", Character.BELLE);
        response = template.postForObject(uriBuilder.build().encode().toUri(), HttpMethod.POST, String.class);
        Assert.assertThat(response, containsString("\"status\":400"));

        // Check if game is already full
        Player player3 = playerBuilder.init().build();
        Player player4 = playerBuilder.init().build();
        Player player5 = playerBuilder.init().build();
        Player player6 = playerBuilder.init().build();

        context = base + "games/" + game.getId() + "/players";
        uriBuilder = UriComponentsBuilder.fromHttpUrl(context)
                .queryParam("token", player3.getToken())
                .queryParam("character", Character.GHOST);
        response = template.postForObject(uriBuilder.build().encode().toUri(), HttpMethod.POST, String.class);
        Assert.assertNotEquals(response, BAD_RESPONSE);

        uriBuilder = UriComponentsBuilder.fromHttpUrl(context)
                .queryParam("token", player4.getToken())
                .queryParam("character", Character.DJANGO);
        response = template.postForObject(uriBuilder.build().encode().toUri(), HttpMethod.POST, String.class);
        Assert.assertNotEquals(response, BAD_RESPONSE);

        uriBuilder = UriComponentsBuilder.fromHttpUrl(context)
                .queryParam("token", player5.getToken())
                .queryParam("character", Character.CHEYENNE);
        response = template.postForObject(uriBuilder.build().encode().toUri(), HttpMethod.POST, String.class);
        Assert.assertNotEquals(response, BAD_RESPONSE);

        uriBuilder = UriComponentsBuilder.fromHttpUrl(context)
                .queryParam("token", player6.getToken())
                .queryParam("character", Character.TUCO);
        response = template.postForObject(uriBuilder.build().encode().toUri(), HttpMethod.POST, String.class);
        Assert.assertThat(response, is(BAD_RESPONSE));

        game = gameRepo.findOne(game.getId());
        Assert.assertThat(game.getPlayers().size(), is(4));


    }

    @Test
    public void testListPlayersForGame() {
        Player player1 = playerBuilder.init().addCharacter(Character.BELLE).build();
        Player player2 = playerBuilder.init().addCharacter(Character.GHOST).build();

        Game game = gameBuilder.init("testListPlayersForGame", "testListPlayersForGame")
                .addUsers(player1, player2)
                .build();

        String context = base + "games/" + game.getId() + "/players";

        Player[] response = template.getForObject(context, Player[].class);

        Assert.assertThat(response.length, is(2));
        Assert.assertThat(player1.getCharacter(), is(Character.BELLE));
        Assert.assertThat(player2.getCharacter(), is(Character.GHOST));
        Assert.assertThat(player1.getId(), is(response[0].getId()));
        Assert.assertThat(player2.getId(), is(response[1].getId()));

        // test wrong game id
        context = base + "games/" + -1 + "/players";
        response = template.getForObject(context, Player[].class);
        Assert.assertThat(response.length, is(0));
    }
}
