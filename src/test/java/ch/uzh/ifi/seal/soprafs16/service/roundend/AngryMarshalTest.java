package ch.uzh.ifi.seal.soprafs16.service.roundend;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.constant.RoundEndEvent;
import ch.uzh.ifi.seal.soprafs16.dto.TurnDTO;
import ch.uzh.ifi.seal.soprafs16.model.*;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.RoundRepository;
import ch.uzh.ifi.seal.soprafs16.service.ActionPhaseService;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Created by mirkorichter on 18.05.16.
 */
public class AngryMarshalTest {

    private static final Logger logger = LoggerFactory.getLogger(RebellionTest.class);

    @InjectMocks
    private ActionPhaseService actionService;

    @Mock
    private GameRepository mockedGameRepo;

    @Mock
    private RoundRepository mockedRoundRepo;

    @Mock
    private PlayerRepository mockedPlayerRepo;



    private Game game;
    private Round round1;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);


        game = new Game();
        game.setId(1L);
        game.setRoundId(1);
        game.setNrOfCars(4);
        game.setCurrentPlayerId(1L);

        Player player1 = new Player();
        player1.setId(1L);
        player1.setUsername("P1");
        player1.setCar(1);
        player1.setLevel(Positionable.Level.BOTTOM);
        player1.setCharacter(Character.BELLE);

        Player player2 = new Player();
        player2.setId(2L);
        player2.setUsername("P2");
        player2.setCar(3);
        player2.setLevel(Positionable.Level.TOP);
        player2.setCharacter(Character.DJANGO);

        Player player3 = new Player();
        player3.setId(3L);
        player3.setUsername("P3");
        player3.setCar(3);
        player3.setLevel(Positionable.Level.TOP);
        player3.setCharacter(Character.DOC);

        game.addPlayer(player1);
        game.addPlayer(player2);
        game.addPlayer(player3);

        round1 = new Round(game.getId(), game.getRoundId(), null, null, "");
        round1.setEnd(RoundEndEvent.ANGRY_MARSHAL);


        when(mockedGameRepo.findOne(1L)).thenReturn(game);
        when(mockedRoundRepo.findByGameIdAndNthRound(game.getId(), 1)).thenReturn(round1);
        when(mockedPlayerRepo.findOne(1L)).thenReturn(player1);
        when(mockedPlayerRepo.findOne(2L)).thenReturn(player2);
        when(mockedPlayerRepo.findOne(3L)).thenReturn(player3);

    }

    @Test
    public void testPlayersOnTopOfMarshalGetShot() {

        game.setPositionMarshal(3);

        round1.addNewlyPlayedCard(new Card(CardType.DRAW,1L));
        round1.addNewlyPlayedCard(new Card(CardType.DRAW,2L));
        round1.addNewlyPlayedCard(new Card(CardType.DRAW,3L));

        TurnDTO dto = new TurnDTO();

        actionService.executeDTO(1L, dto);
        actionService.executeDTO(1L, dto);
        actionService.executeDTO(1L, dto);

        List<Player> players = mockedGameRepo.findOne(1L).getPlayers();

        Assert.assertEquals(0,players.get(0).getInjuries());
        Assert.assertEquals(1,players.get(1).getInjuries());
        Assert.assertEquals(1,players.get(2).getInjuries());
        Assert.assertEquals(3,game.getPositionMarshal());

    }

    @Test
    public void testMarshalsMoves() {

        game.setPositionMarshal(1);
        game.getPlayers().get(0).setLevel(Positionable.Level.TOP);

        Assert.assertEquals(1,game.getPositionMarshal());
        Assert.assertEquals(Positionable.Level.TOP, game.getPlayers().get(0).getLevel());

        round1.addNewlyPlayedCard(new Card(CardType.DRAW,1L));
        round1.addNewlyPlayedCard(new Card(CardType.DRAW,2L));
        round1.addNewlyPlayedCard(new Card(CardType.DRAW,3L));

        TurnDTO dto = new TurnDTO();

        actionService.executeDTO(1L, dto);
        actionService.executeDTO(1L, dto);
        actionService.executeDTO(1L, dto);

        List<Player> players = mockedGameRepo.findOne(1L).getPlayers();

        Assert.assertEquals(1,players.get(0).getInjuries());
        Assert.assertEquals(0,players.get(1).getInjuries());
        Assert.assertEquals(0,players.get(2).getInjuries());
        Assert.assertEquals(2,game.getPositionMarshal());

    }
}
