package ch.uzh.ifi.seal.soprafs16.model;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Defines a card.
 * Created by mirkorichter on 22.03.16.
 */
@Entity
public class Card implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * Defines type of this card.
     */
    @Column
    private CardType type;

    /**
     * Defines owner of this card
     */
    @ManyToOne
    @JsonBackReference
    private Player owner;

    @Column
    private boolean onHand;

    public boolean isOnHand() {
        return onHand;
    }

    public void setOnHand(boolean onHand) {
        this.onHand = onHand;
    }

    public Card() {
        onHand = false;
    }

    public Card(CardType type, Player owner) {
        this.type = type;
        this.owner = owner;
    }

    /**
     * Determines whether card was played face up or face down
     */
    boolean isFaceUp = true;

    /**
     * Returns owner of this card.
     *
     * @return owner.
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Sets owner for this card
     *
     * @param owner player or game
     */
    public void setOwner(Player owner) {
        this.owner = owner;
    }

    /**
     * Returns type of this card.
     *
     * @return
     */
    public CardType getType() {
        return type;
    }

    /**
     * Sets type for this card.
     *
     * @param type on of CardType.
     */
    public void setType(CardType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Card)) {
            return false;
        }

        Card card = (Card) o;
        return (card.getOwner() == this.getOwner() && card.getType() == this.getType());
    }

    @Override
    public String toString() {
        return "Card{" +
                "type=" + type +
                '}';
    }
}
