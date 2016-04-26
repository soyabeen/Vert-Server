package ch.uzh.ifi.seal.soprafs16.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Defines a card deck for a player.
 * Created by mirkorichter on 22.03.16.
 */
@Entity
public class CardDeck implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * List of cards in this deck.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Card> deck;

    protected CardDeck() {
    }

    public CardDeck(List<Card> bunchOfCards) {
        this.deck = bunchOfCards;
    }

    /**
     * Add a card to this deck.
     * @param card to add in deck.
     */
    public void addCard(Card card) {
        deck.add(card);
    }

    /**
     * Draws n many cards form deck randomly.
     * @param numOfCards defines how many cards to draw.
     * @return returns ArrayList of cards on hand of player.
     */
    public List<Card> drawCard(int numOfCards) {
        List<Card> drawnCards = new ArrayList<>();
        List<Integer> random = new ArrayList<>();

        for(int i = 0; i < deck.size(); i++) {
            random.add(i);
        }

        Collections.shuffle(drawnCards);

        for(int i = 0; i < numOfCards; i++) {
            drawnCards.add(deck.get(random.get(i)));
        }
        return drawnCards;
    }

    public void shuffle() {
        Collections.shuffle(deck);
    }

    @Override
    public String toString() {
        return "CardDeck{" +
                "deck=" + deck +
                '}';
    }
}
