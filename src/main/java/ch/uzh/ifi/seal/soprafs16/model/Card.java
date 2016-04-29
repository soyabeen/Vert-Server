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
//    @ManyToOne
//    @JsonBackReference
//    private Player owner;

    @Column
    private Long ownerId;

    @Column
    private boolean onHand;

    /**
     * Determines whether card was played face up or face down
     */
    @Column
    boolean isFaceDown;

    public boolean isOnHand() {
        return onHand;
    }

    public void setOnHand(boolean onHand) {
        this.onHand = onHand;
    }

    public Card() {
        onHand = false;
    }

    public Card(CardType type, Long ownerId) {
        this.type = type;
        this.ownerId = ownerId;
    }

    /**
     * Returns owner of this card.
     *
     * @return ownerId.
     */
    public Long getOwnerId() {
        return ownerId;
    }

    /**
     * Sets owner for this card
     *
     * @param ownerId player or game
     */
    public void setOwnerId(Long ownerId) {
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
        return (card.getOwnerId() == this.getOwnerId() && card.getType() == this.getType());
    }

    @Override
    public String toString() {
        return "Card{" +
                "type=" + type +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
