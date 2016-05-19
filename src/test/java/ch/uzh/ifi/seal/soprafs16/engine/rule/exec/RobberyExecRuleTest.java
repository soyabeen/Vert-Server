package ch.uzh.ifi.seal.soprafs16.engine.rule.exec;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.constant.LootType;
import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.helper.PositionedLoot;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Loot;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by soyabeen on 19.05.16.
 */
public class RobberyExecRuleTest {

    @Test
    public void checkEvalFalse() {

        Player player = new Player();

        Loot l1 = new PositionedLoot().builder()
                .id(1L)
                .withType(LootType.PURSE_SMALL).build();

        Loot l2 = new PositionedLoot().builder()
                .id(2L)
                .withType(LootType.JEWEL).build();

        Loot l3 = new PositionedLoot().builder()
                .id(3L)
                .withType(LootType.STRONGBOX).build();

        Loot ltarget = new PositionedLoot().builder()
                .id(100L)
                .withType(LootType.JEWEL).build();

        Game game = new Game();
        game.addLoot(l1);
        game.addLoot(l2);
        game.addLoot(l3);

        ActionCommand command = new ActionCommand(CardType.ROBBERY, game, player, null);
        command.setTargetLoot(ltarget);
        Assert.assertFalse(new RobberyExecRule().evaluate(command));
    }
}
