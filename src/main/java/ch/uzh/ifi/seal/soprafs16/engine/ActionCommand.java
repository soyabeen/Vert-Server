package ch.uzh.ifi.seal.soprafs16.engine;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.constant.Direction;
import ch.uzh.ifi.seal.soprafs16.model.Loot;
import ch.uzh.ifi.seal.soprafs16.model.Player;

/**
 * Created by soyabeen on 20.04.16.
 */
public class ActionCommand {

    private CardType card;
    // from repo
    private Player currentPlayer;
    // chosen possibility
    private Player targetPlayer;
    // chosen possiblity
    private Loot targetLoot;
    private Direction direction;

}
