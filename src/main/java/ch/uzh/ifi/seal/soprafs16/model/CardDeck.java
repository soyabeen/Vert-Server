package ch.uzh.ifi.seal.soprafs16.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

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
    @OneToMany
    private ArrayList<Card> deck = new ArrayList<Card>(10);

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
    public ArrayList<Card> drawCard(int numOfCards) {
        ArrayList<Card> drawnCards = new ArrayList<Card>();
        ArrayList<Integer> random = new ArrayList<Integer>();

        for(int i = 0; i < deck.size(); i++) {
            random.add(i);
        }

        Collections.shuffle(drawnCards);

        for(int i = 0; i < numOfCards; i++) {
            drawnCards.add(deck.get((random.get(i))));
        }
        return drawnCards;
    }



}
