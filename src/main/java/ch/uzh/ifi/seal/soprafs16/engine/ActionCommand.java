package ch.uzh.ifi.seal.soprafs16.engine;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.constant.Direction;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Loot;
import ch.uzh.ifi.seal.soprafs16.model.Player;

/**
 * Created by soyabeen on 20.04.16.
 */
public class ActionCommand {

    private CardType card;
    private Game game;
    // from repo
    private Player currentPlayer;
    // chosen possibility
    private Player targetPlayer;
    // chosen possiblity
    private Loot targetLoot;
    private Direction direction;

    public ActionCommand(CardType card, Game game, Player currentPlayer, Player targetPlayer) {
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

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getTargetPlayer() {
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



}
