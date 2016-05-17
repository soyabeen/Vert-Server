package ch.uzh.ifi.seal.soprafs16.engine;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.helper.PositionedPlayer;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by soyabeen on 27.04.16.
 */
public class GameEngineIntegrationTest {

    @Test
    public void callEngine() throws InvocationTargetException {
        Player actor = PositionedPlayer.builder().withUserName("actor").onUpperLevelAt(1).build();
        Player target = PositionedPlayer.builder().withUserName("actor").onUpperLevelAt(0).build();

        Game game = new Game();
        game.setName("engine test game");
        game.setNrOfCars(3);
        game.addPlayer(actor);

        ActionCommand command = new ActionCommand(CardType.MOVE, game, actor, target);
        GameEngine engine = new GameEngine();
        engine.executeAction(command);
    }

    @Test
    public void callEngineForUnknownCardType() throws InvocationTargetException {
        Player actor = PositionedPlayer.builder().withUserName("actor").onUpperLevelAt(1).build();
        Player target = PositionedPlayer.builder().withUserName("actor").onUpperLevelAt(0).build();

        Game game = new Game();
        game.setName("engine test game");
        game.setNrOfCars(3);
        game.addPlayer(actor);

        ActionCommand command = new ActionCommand(CardType.BULLET, game, actor, target);
        GameEngine engine = new GameEngine();
        try {
            engine.executeAction(command);
            Assert.fail("Unknown card type should have forced an exception.");
        }catch (IllegalArgumentException iae) {
            Assert.assertTrue("Could not create RuleSet for unknown type.", iae.getMessage().contains("Could not create RuleSet"));
        }
    }
}
