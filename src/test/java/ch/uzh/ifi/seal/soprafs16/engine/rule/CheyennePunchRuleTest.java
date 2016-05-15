package ch.uzh.ifi.seal.soprafs16.engine.rule;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.constant.LootType;
import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.engine.rule.replace.CheyennePunchRepRule;
import ch.uzh.ifi.seal.soprafs16.helper.PlayerBuilder;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Loot;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;

/**
 * Created by devuser on 15.05.2016.
 */
public class CheyennePunchRuleTest {

    Game game;
    Player django, tuco, cheyenne;
    List<Positionable> actors;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        // set up test
        game = new Game();

        PlayerBuilder playerbuilder = new PlayerBuilder();
        django = playerbuilder.init().addCharacter(Character.DJANGO).build();
        Loot django_loot = new Loot(LootType.PURSE_BIG, game.getId(), 500);
        django.addLoot(django_loot);

        playerbuilder = new PlayerBuilder();
        tuco = playerbuilder.init().addCharacter(Character.TUCO).build();
        Loot tuco_loot = new Loot(LootType.JEWEL, game.getId(), 1000);
        django.addLoot(tuco_loot);

        playerbuilder = new PlayerBuilder();
        cheyenne = playerbuilder.init().getPlayerNoPersistence(Character.CHEYENNE);

        Loot droppedLoot = new Loot(LootType.STRONGBOX, game.getId(), 9001);

        actors = new ArrayList<>();
        actors.add(django);
        actors.add(tuco);
        actors.add(droppedLoot);

        game.addPlayer(django);
        game.addPlayer(tuco);
        game.addPlayer(cheyenne);
    }

    @Test
    public void testEvaluateRule() {

        ActionCommand ac = new ActionCommand(CardType.PUNCH, game, cheyenne, tuco);
        CheyennePunchRepRule cheyenneRule = new CheyennePunchRepRule(ac);

        Assert.assertEquals(cheyenneRule.evaluate(actors), true);
    }

    @Test
    public void testReplaceRule() {
        List<Positionable> result = new ArrayList<>();

        ActionCommand ac = new ActionCommand(CardType.PUNCH, game, cheyenne, tuco);
        CheyennePunchRepRule cheyenneRule = new CheyennePunchRepRule(ac);
        result = cheyenneRule.replace(actors);

        Assert.assertThat(result.size(), is(4));

        for (Positionable p : result) {
            if(p instanceof Player && ((Player) p).getCharacter().equals(Character.CHEYENNE)) {
                Assert.assertThat(((Player) p).getLoots().size(), is(1));
                Assert.assertEquals(((Player) p).getLoots().get(0).getType(), LootType.STRONGBOX);
            }
        }
    }
}
