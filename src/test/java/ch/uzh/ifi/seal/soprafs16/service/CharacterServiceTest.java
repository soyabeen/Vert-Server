package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;

/**
 *
 * Created by alexanderhofmann on 24/03/16.
 */
public class CharacterServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(CharacterServiceIntegrationTest.class);

    /**
     * Injects mocks annotated with @Mock into characterService.
     */
    @InjectMocks
    private CharacterService characterService;

    /**
     * Mock to be used when calling playerService.listPlayersForGame(1L).
     */
    @Mock
    private PlayerService playerService;

    /**
     * Must init mocks.
     */
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAll7UnfilteredCharacters() {
        Assert.assertThat(characterService.listCharacters().size(), is(7));
        Assert.assertThat(characterService.listCharacters(), hasItems(Character.values()));
    }

    @Test
    public void getAvailableCharacters() {
        Player p1 = new Player();
        p1.setCharacter(Character.BELLE);
        Player p2 = new Player();
        p2.setCharacter(Character.GHOST);
        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);

        // When playerService.listPlayersForGame(1L) is called in characterService
        // return players list.
        when(playerService.listPlayersForGame(1L)).thenReturn(players);

        List<Character> result = characterService.listAvailableCharactersByGame(1L);

        Assert.assertThat(result.size(), is(5));
        Assert.assertThat(result, hasItem(Character.CHEYENNE));
        Assert.assertTrue(!result.contains(Character.GHOST));
        Assert.assertTrue(!result.contains(Character.BELLE));
    }
}
