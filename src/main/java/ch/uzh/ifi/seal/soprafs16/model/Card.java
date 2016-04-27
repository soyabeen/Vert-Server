package ch.uzh.ifi.seal.soprafs16.model;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;

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
    @Column
    private Long ownerId;

    public Card() { }

    public Card(CardType type, Long ownerId) {
        this.type = type;
        this.ownerId = ownerId;
    }

    /**
     * Determines whether card was played face up or face down
     */
    boolean isFaceUp = true;

    /**
     * Returns owner of this card.
     *
     * @return ownerId.
     */
    public Long getOwner() {
        return ownerId;
    }

    /**
     * Sets owner for this card
     *
     * @param ownerId player or game
     */
    public void setOwner(Long ownerId) {
        this.ownerId = ownerId;
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
}
