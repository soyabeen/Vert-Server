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

    @ManyToOne
    private Meeple owner;

    /**
     * Returns owner of this card.
     * @return owner.
     */
    public Meeple getOwner() {
        return owner;
    }

    /**
     * Sets owner for this card
     * @param owner player or game
     */
    public void setOwner(Meeple owner) {
        this.owner = owner;
    }

    /**
     * Returns type of this card.
     * @return
     */
    public CardType getType() {
        return type;
    }

    /**
     * Sets type for this card.
     * @param type on of CardType.
     */
    public void setType(CardType type) {
        this.type = type;
    }
}
