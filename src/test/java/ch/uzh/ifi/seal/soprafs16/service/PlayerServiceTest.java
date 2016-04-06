package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;

/**
 * Created by antoio on 3/26/16.
 */
public class PlayerServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(CharacterServiceIntegrationTest.class);


    @InjectMocks
    private PlayerService playerService;

    @Mock
    private GameRepository gameRepo;

    @Mock
    private UserRepository userRepo;

    private Game game;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        game = new Game();
        when(gameRepo.findOne(1L)).thenReturn(game);
    }

    @Test
    public void testListPlayers() {
        Player p1 = new Player();
        p1.setCharacter(Character.BELLE);
        Player p2 = new Player();
        p2.setCharacter(Character.GHOST);

        User user1 = new User("abc", "def");
        user1.setToken(UUID.randomUUID().toString());
        user1.setPlayer(p1);

        User user2 = new User("def", "abc");
        user2.setToken(UUID.randomUUID().toString());
        user2.setPlayer(p2);
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        game.addUser(user1);
        game.addUser(user2);

        List<Player> players = playerService.listPlayersForGame(1L);

        Assert.assertThat(players.size(), is(2));
        Assert.assertThat(players.get(0).getCharacter(), is(Character.BELLE));
        Assert.assertThat(players.get(1).getCharacter(), is(Character.GHOST));
    }
}
