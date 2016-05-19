package ch.uzh.ifi.seal.soprafs16.engine.rule;

import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.constant.LootType;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Loot;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import ch.uzh.ifi.seal.soprafs16.service.CharacterServiceIntegrationTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;

/**
 * Created by devuser on 16.05.2016.
 */
public class PunchRuleSetTest {

    private static final Logger logger = LoggerFactory.getLogger(CharacterServiceIntegrationTest.class);

    Game game;
    Player ghost, tuco, belle;
    List<Positionable> result;

    @Before
    public void init() {
        game = new Game();

        Player ghost = new Player();
        ghost.setCharacter(Character.GHOST);
        ghost.setCar(1);
        Loot ghost_loot = new Loot(LootType.PURSE_SMALL, game.getId(), 200);
        ghost.addLoot(ghost_loot);

        Player tuco = new Player();
        tuco.setCharacter(Character.TUCO);
        tuco.setCar(1);
        Loot tuco_loot = new Loot(LootType.PURSE_BIG, game.getId(), 500);
        tuco.addLoot(tuco_loot);

        Player belle = new Player();
        belle.setCharacter(Character.BELLE);
        belle.setCar(1);
        Loot belle_loot = new Loot(LootType.JEWEL, game.getId(), 1000);
        belle.addLoot(belle_loot);

        game.addPlayer(ghost);
        game.addPlayer(tuco);
        game.addPlayer(belle);

    }

    @Test
    public void testPunchRuleSimulate() {
        init();
        PunchRuleSet prs = new PunchRuleSet();
        List<Positionable> players = new ArrayList<>(game.getPlayers());
        result = prs.simulate(game, players.get(0));
        logger.debug("Character " + players.get(0) + "\t is in " + players.get(0).getCar() + " before punch");
        Assert.assertThat(result.size(), is(0));
        logger.debug("Character " + players.get(0) + "\t is in " + players.get(0).getCar() + " after punch");
    }

    @Test
    public void testPunchRuleExecute() {
        // Trailer. Will hit theaters soon.
    }
}
