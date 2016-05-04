package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.Application;
import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.helper.PlayerBuilder;
import ch.uzh.ifi.seal.soprafs16.model.Card;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Move;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Optional;

import static org.hamcrest.core.Is.is;

/**
 * Created by soyabeen on 04.05.16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class LastPlayedCardIntegrationTest {

    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private RoundService roundService;

    @Autowired
    private PlayerBuilder playerBuilder;


    private Card getAPlayableCard (Player p, int id) {
        for(Card c : p.getDeck()) {
            if(c.isOnHand() && c.getId() > id) {
                return c;
            }
        }
        return null;
    }

    private Move buildMove(Game game, Player player, Card card) {
        Move m = new Move();
        m.setGame(game);
        m.setPlayer(player);
        m.setPlayedCard(card);
        return m;
    }

    @Test
    public void playedCardInCorrectOrder () {

        boolean isPersistent = true;

        Player p1 = playerBuilder.init(isPersistent, "Player-1").build();

        Game game = new Game();
        game.setName("LastPlayedCardTestGame");
        Game savedGame = gameService.createGame(game, p1.getToken(), 2);

        Player p2 = playerBuilder.init(isPersistent, "Player-2").build();
        playerService.assignPlayer(savedGame.getId(), p2, Character.DJANGO);
        playerService.initializeCharacter(savedGame.getId(), p1, Character.TUCO);
        gameService.startGame(savedGame.getId(), p1.getToken());

        Player playable1 = playerService.getPlayer(p1.getId());
        Player playable2 = playerService.getPlayer(p2.getId());
        Game playableGame = gameService.loadGameFromRepo(savedGame.getId());

        // play card 1
        Card c1 = getAPlayableCard(playable1, 0);
        Move m1 = buildMove(playableGame, playable1, c1);
        roundService.makeAMove(playableGame.getId(), playableGame.getRoundId(), m1);

        Optional<Card> o1 = gameService.getLastPlayedCardForGame(playableGame.getId());
        Assert.assertTrue("First played card optional not empty.", o1.isPresent());
        Assert.assertThat("First played card id.", o1.get().getId(), is(c1.getId()));
        Assert.assertThat("First played card type.", o1.get().getType(), is(c1.getType()));

        // play card 2
        Card c2 = getAPlayableCard(playable2, 0);
        Move m2 = buildMove(playableGame, playable2, c2);
        roundService.makeAMove(playableGame.getId(), playableGame.getRoundId(), m2);

        Optional<Card> o2 = gameService.getLastPlayedCardForGame(playableGame.getId());
        Assert.assertTrue("Second played card optional not empty.", o2.isPresent());
        Assert.assertThat("Second played card id.", o2.get().getId(), is(c2.getId()));
        Assert.assertThat("Second played card type.", o2.get().getType(), is(c2.getType()));

        // play card 3
        Card c3 = getAPlayableCard(playable1, Integer.parseInt(c1.getId()+""));
        Move m3 = buildMove(playableGame, playable1, c3);
        roundService.makeAMove(playableGame.getId(), playableGame.getRoundId(), m3);

        Optional<Card> o3 = gameService.getLastPlayedCardForGame(playableGame.getId());
        Assert.assertTrue("Third played card optional not empty.", o3.isPresent());
        Assert.assertThat("Third played card id.", o3.get().getId(), is(c3.getId()));
        Assert.assertThat("Third played card type.", o3.get().getType(), is(c3.getType()));
    }
}
