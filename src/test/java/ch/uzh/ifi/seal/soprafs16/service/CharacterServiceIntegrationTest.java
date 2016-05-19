package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.Application;
import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.helper.GameBuilder;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;

/**
 * Created by soyabeen on 24.03.16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class CharacterServiceIntegrationTest {

    @SuppressWarnings("unused")
    private static final Logger logger  = LoggerFactory.getLogger(CharacterServiceIntegrationTest.class);

    @Autowired
    private CharacterService characterService;

    @Autowired
    private GameBuilder gameBuilder;

    @Test
    public void getAll7UnfilteredCharacters() {
        Assert.assertThat(characterService.listCharacters().size(), is(7));
        Assert.assertThat(characterService.listCharacters(), hasItems(Character.values()));
    }

    @Test
    public void getAvailableCharacters() {
        gameBuilder.init("getAvailableCharacters", "getAvailableCharacters");
        Game game = gameBuilder.addRandomUserAndPlayer(Character.BELLE).addRandomUserAndPlayer(Character.GHOST).build();

        List<Character> result = characterService.listAvailableCharactersByGame(game.getId());
        Assert.assertThat(result.size(), is(5));
        Assert.assertThat(result, hasItem(Character.CHEYENNE));
        Assert.assertTrue(!result.contains(Character.GHOST));
        Assert.assertTrue(!result.contains(Character.BELLE));
    }
}
