package ch.uzh.ifi.seal.soprafs16.service.roundend;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.constant.LootType;
import ch.uzh.ifi.seal.soprafs16.constant.RoundEndEvent;
import ch.uzh.ifi.seal.soprafs16.dto.TurnDTO;
import ch.uzh.ifi.seal.soprafs16.model.*;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.LootRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.RoundRepository;
import ch.uzh.ifi.seal.soprafs16.service.ActionPhaseService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

/**
 * Created by devuser on 18.05.2016.
 */
public class MarshalsRevengeTest {

    private static final Logger logger = LoggerFactory.getLogger(MarshalsRevengeTest.class);

    @InjectMocks
    private ActionPhaseService actionService;

    @Mock
    private GameRepository mockedGameRepo;

    @Mock
    private RoundRepository mockedRoundRepo;

    @Mock
    private PlayerRepository mockedPlayerRepo;

    @Mock
    private LootRepository mockedLootRepo;

    private Game game;
    private Round round1;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);


        game = new Game();
        game.setId(1L);
        game.setRoundId(1);
        game.setNrOfCars(4);
        game.setCurrentPlayerId(1L);
        game.setPositionMarshal(2);

        Player player1 = new Player();
        player1.setId(1L);
        player1.setUsername("P1");
        player1.setCar(2);
        player1.setLevel(Positionable.Level.TOP);
        player1.setCharacter(Character.BELLE);
        Loot l1 = new Loot(LootType.PURSE_SMALL, game.getId(), 250);
        l1.setOwnerId(1L);
        l1.setId(1L);
        Loot l2 = new Loot(LootType.PURSE_BIG, game.getId(), 350);
        l2.setOwnerId(1L);
        l2.setId(2L);
        player1.addLoot(l1);
        player1.addLoot(l2);
        game.addPlayer(player1);

        Player player2 = new Player();
        player2.setId(2L);
        player2.setUsername("P2");
        player2.setCar(2);
        player2.setLevel(Positionable.Level.TOP);
        player2.setCharacter(Character.DJANGO);
        Loot l3 = new Loot(LootType.PURSE_BIG, game.getId(), 500);
        l3.setId(3L);
        l3.setOwnerId(2L);
        player2.addLoot(l3);
        // this loot should not be lost so no ID is set
        player2.addLoot(new Loot(LootType.JEWEL, game.getId(), 500));
        game.addPlayer(player2);

        Player player3 = new Player();
        player3.setId(3L);
        player3.setUsername("P3");
        player3.setCar(2);
        player3.setLevel(Positionable.Level.TOP);
        player3.setCharacter(Character.TUCO);
        // this loot should not be lost so no ID is set
        player3.addLoot(new Loot(LootType.STRONGBOX, game.getId(), 500));
        player3.addLoot(new Loot(LootType.JEWEL, game.getId(), 500));
        game.addPlayer(player3);


        round1 = new Round(game.getId(), game.getRoundId(), null, null, "");
        round1.setEnd(RoundEndEvent.MARSHALS_REVENGE);

        when(mockedGameRepo.findOne(1L)).thenReturn(game);
        when(mockedRoundRepo.findByGameIdAndNthRound(game.getId(), 1)).thenReturn(round1);
        when(mockedPlayerRepo.findOne(1L)).thenReturn(player1);
        when(mockedPlayerRepo.findOne(2L)).thenReturn(player2);
        when(mockedPlayerRepo.findOne(3L)).thenReturn(player3);
        when(mockedLootRepo.findOne(1L)).thenReturn(l1);
        when(mockedLootRepo.findOne(2L)).thenReturn(l2);
        when(mockedLootRepo.findOne(3L)).thenReturn(l3);
    }

    @Test
    public void testPlayersOnTopOfMarshalLosePurse() {

        round1.addNewlyPlayedCard(new Card(CardType.DRAW, 1L));
        round1.addNewlyPlayedCard(new Card(CardType.DRAW, 2L));
        round1.addNewlyPlayedCard(new Card(CardType.DRAW, 3L));

        TurnDTO dto = new TurnDTO();

        actionService.executeDTO(1L, dto);
        actionService.executeDTO(1L, dto);
        actionService.executeDTO(1L, dto);

        List<Player> players = mockedGameRepo.findOne(1L).getPlayers();

        // debug
        for (Player p : players) {
            logger.debug("Loots of " + p.getCharacter() + ": " + p.getLoots());
        }

        Player player1 = players.get(0);
        Assert.assertThat(player1.getLoots().size(), is(1));
        Assert.assertThat(player1.getLoots().get(0).getType(), is(LootType.PURSE_BIG));

        Player player2 = players.get(1);
        Assert.assertThat(player2.getLoots().size(), is(1));
        Assert.assertThat(player2.getLoots().get(0).getType(), is(LootType.JEWEL));

        Player player3 = players.get(2);
        Assert.assertThat(player3.getLoots().size(), is(2));
        Assert.assertThat(player3.getLoots().get(0).getType(), is(LootType.STRONGBOX));
        Assert.assertThat(player3.getLoots().get(1).getType(), is(LootType.JEWEL));

        // Test Marshal did not move
        Assert.assertThat(mockedGameRepo.findOne(1L).getPositionMarshal(), is(2));
    }

}