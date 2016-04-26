package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.Application;
import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs16.constant.LootType;
import ch.uzh.ifi.seal.soprafs16.helper.GameBuilder;
import ch.uzh.ifi.seal.soprafs16.helper.PlayerBuilder;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Loot;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
import org.hibernate.Hibernate;
import org.hibernate.validator.cfg.defs.AssertTrueDef;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.print.attribute.standard.PrinterURI;
import java.net.MalformedURLException;
import java.net.URL;

import static org.hamcrest.CoreMatchers.is;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class GameCommandControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(GameCommandControllerTest.class);

    @Value("${local.server.port}")
    private int port;

    private URL base;
    private RestTemplate template;

    @Autowired
    private PlayerRepository playerRepo;

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
        playerRepo.deleteAll();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void addGameTest() {
        // test valid query
        Player player = playerBuilder.init(true).build();
        Game game = gameBuilder.initNoPersistence("addGameTest", player.getUsername()).build();

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(base.toString())
                .queryParam("token", player.getToken());

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


        ResponseEntity<ErrorResource> errorResponse = template.exchange(uriBuilder.build().encode().toUri(),
                HttpMethod.POST,
                gameEntity,
                ErrorResource.class);

        Assert.assertThat(errorResponse.getStatusCode(), is(HttpStatus.PRECONDITION_FAILED));
    }

    @Test
    public void startGame() throws MalformedURLException {
        Player p1 = playerBuilder.init(true).addCharacter(Character.BELLE).build();
        Player p2 = playerBuilder.init(true).addCharacter(Character.DOC).build();
        Game game = gameBuilder.init("startGame", p1.getUsername()).addUser(p1).addUser(p2).build();
        game.setOwner(p1.getUsername());
        game = gameRepo.save(game);

        URL url = new URL(base.toString() + "/" + game.getId() + "/start");
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url.toString()).queryParam("token", p1.getToken());
        template.postForLocation(uriBuilder.build().encode().toUri(), null);

        logger.info(game.toString());
        game = gameRepo.findOne(game.getId());
        Assert.assertThat(game.getStatus(), is(GameStatus.PLANNINGPHASE));

        // Players initialized correctly.
        for (Player p : game.getPlayers()) {
            // Has a deck
            Assert.assertNotNull(p.getDeck());

            // Has a purse with value 250
            Assert.assertThat(p.getLoots().size(), is(1));
            Loot loot = p.getLoots().get(0);
            Assert.assertThat(loot.getValue(), is(250));
            Assert.assertThat(loot.getType(), is(LootType.PURSE_SMALL));

            logger.error(p.toString());
            // Has 6 (7) cards in hand
            if (p.getCharacter().equals(Character.DOC)) {
                Assert.assertThat(p.getHand().size(), is(7));
            } else {
                Assert.assertThat(p.getHand().size(), is(6));
            }
        }

        Player p3 = game.getPlayers().get(0);
        logger.info(p3.getDeck().toString());
    }
}
