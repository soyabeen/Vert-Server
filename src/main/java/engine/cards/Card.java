package engine.cards;

import engine.User;
/**
 * Created by alex on 17.03.2016.
 */
public abstract class Card {

    private CardType type;

    private User owner;

    public Card(CardType type, User owner) {
        this.type = type;
        this.owner = owner;
    }

    public CardType getType() {
        return type;
    }

    public User getOwner() {
        return owner;
    }
}
