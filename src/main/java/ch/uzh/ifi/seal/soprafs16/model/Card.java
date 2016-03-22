package ch.uzh.ifi.seal.soprafs16.model;

/**
 * Created by mirkorichter on 22.03.16.
 */
public class Card {

    private CardType type;

    private long owner;

    public long getOwner() {
        return owner;
    }

    public void setOwner(long owner) {
        this.owner = owner;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }
}
