package ch.uzh.ifi.seal.soprafs16.engine.rule;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.constant.LootType;
import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.helper.PositionedLoot;
import ch.uzh.ifi.seal.soprafs16.helper.PositionedPlayer;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Loot;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.hamcrest.core.Is.is;

/**
 * Created by soyabeen on 27.04.16.
 */
public class RobberyRuleSetTest {

    @Test
    public void findSimLootsOnFloor() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        RuleSet mrs = RuleSet.createRuleSet(CardType.ROBBERY);
        Game game = new Game();
        game.setId(1L);
        game.setNrOfCars(3);

        Player player = PositionedPlayer.builder()
                .withUserName("actor")
                .onLowerLevelAt(1).build();

        Loot lootOnSameFloor1 = PositionedLoot.builder()
                .withType(LootType.PURSE_SMALL)
                .onLowerLevelAt(1).build();
        game.addLoot(lootOnSameFloor1);

        Loot lootOnSameFloor2 = PositionedLoot.builder()
                .withType(LootType.JEWEL)
                .onLowerLevelAt(1).build();
        game.addLoot(lootOnSameFloor2);

        Loot l1 = PositionedLoot.builder()
                .withType(LootType.STRONGBOX)
                .onUpperLevelAt(1).build();
        game.addLoot(l1);

        Loot l2 = PositionedLoot.builder()
                .withType(LootType.STRONGBOX)
                .onLowerLevelAt(2).build();
        game.addLoot(l2);

        List<Positionable> resultList = mrs.simulate(game, player);
        Assert.assertThat(resultList.size(), is(2));
    }

    @Test
    public void findNoLootsOnFloor() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        RuleSet mrs = RuleSet.createRuleSet(CardType.ROBBERY);
        Game game = new Game();
        game.setId(1L);
        game.setNrOfCars(3);

        Player player = PositionedPlayer.builder()
                .withUserName("actor")
                .onLowerLevelAt(1).build();

        Loot lootOnSameFloor1 = PositionedLoot.builder()
                .withType(LootType.PURSE_SMALL)
                .onLowerLevelAt(1).build();
        game.addLoot(lootOnSameFloor1);

        Loot lootOnSameFloor2 = PositionedLoot.builder()
                .withType(LootType.JEWEL)
                .onLowerLevelAt(1).build();
        game.addLoot(lootOnSameFloor2);

        ActionCommand command = new ActionCommand(CardType.ROBBERY, game, player, null);
        command.setTargetLoot(lootOnSameFloor1);

        List<Positionable> resultList = mrs.execute(command);
        Assert.assertThat(resultList.size(), is(2));
    }


    @Test
    public void execRobbery() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        RuleSet mrs = RuleSet.createRuleSet(CardType.ROBBERY);
        Game game = new Game();
        game.setId(1L);
        game.setNrOfCars(3);

        Player player = PositionedPlayer.builder()
                .withUserName("actor")
                .onLowerLevelAt(1).build();

        Loot l1 = PositionedLoot.builder()
                .withType(LootType.STRONGBOX)
                .onUpperLevelAt(1).build();
        game.addLoot(l1);

        Loot l2 = PositionedLoot.builder()
                .withType(LootType.STRONGBOX)
                .onLowerLevelAt(2).build();
        game.addLoot(l2);

        List<Positionable> resultList = mrs.simulate(game, player);
        Assert.assertThat(resultList.size(), is(0));
    }
}
