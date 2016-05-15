package ch.uzh.ifi.seal.soprafs16.engine;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.constant.Direction;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Loot;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;

/**
 * Created by soyabeen on 20.04.16.
 */
public class ActionCommand {

    private CardType card;
    private Game game;
    // from repo
    private Positionable currentPlayer;
    // chosen possibility
    private Positionable targetPlayer;
    // chosen possiblity
    private Loot targetLoot;
    private Direction direction;

    public ActionCommand(CardType card, Game game, Positionable currentPlayer, Positionable targetPlayer) {
        this.card = card;
        this.game = game;
        this.currentPlayer = currentPlayer;
        this.targetPlayer = targetPlayer;
    }

    public CardType getCard() {
        return card;
    }

    public Game getGame() {
        return game;
    }

    public Positionable getCurrentPlayer() {
        return currentPlayer;
    }

    public Positionable getTargetPlayer() {
        return targetPlayer;
    }

    public Loot getTargetLoot() {
        return targetLoot;
    }

    public void setTargetLoot(Loot loot) {
        this.targetLoot = loot;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }



}
