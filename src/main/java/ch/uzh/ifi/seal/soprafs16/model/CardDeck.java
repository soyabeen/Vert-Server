package ch.uzh.ifi.seal.soprafs16.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Defines a card deck for a player.
 * Created by mirkorichter on 22.03.16.
 */
public class CardDeck implements Serializable {


    private Long id;

    /**
     * List of cards in this deck.
     */
    private List<Card> deck;

    public CardDeck() {
        deck = new ArrayList<>();
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
        List<Card> currentDeck = getCardsInDeck();
        List<Card> drawnCards = new ArrayList<>();

        numOfCards = (numOfCards > currentDeck.size()) ? currentDeck.size() : numOfCards;

        Collections.shuffle(currentDeck);

        for(int i = 0; i < numOfCards; i++) {
            Card tmp = currentDeck.get(i);
            drawnCards.add(tmp);
        }
        return drawnCards;
    }

    private List<Card> getCardsInDeck() {
        List<Card> currentDeck = new ArrayList<>();
        for (Card c: deck) {
            if(!c.isOnHand()) currentDeck.add(c);
        }
        return currentDeck;
    }

    public int size() {
        return deck.size();
    }

    public List<Card> getDeck() {
        return deck;
    }

    public void setDeck(List<Card> deck) {
        this.deck = deck;
    }

    public List<Card> getCardsOnHand() {
        List<Card> onHand = new ArrayList<>();
        for(Card c: deck) {
            if(c.isOnHand()) onHand.add(c);
        }
        return onHand;

    }

    public void setCardToOnHand(Card card) {
        int i = 0;
        boolean notFinished =true;
        while(notFinished) {
            if(deck.get(i).getType().equals(card.getType()) && !deck.get(i).isOnHand()) {
                deck.get(i).setOnHand(true);
                notFinished = false;
            }
            ++i;
        }
    }

    public void setNewHand(List<Card> newHand) {

        for(Card c: deck) {
            c.setOnHand(false);
        }
        for(Card c: newHand) {
            setCardToOnHand(c);
        }
    }

    public void removeCardFromHand(Card card) {
        int i = 0;
        while(!(deck.get(i).getType().equals(card.getType()) && deck.get(i).isOnHand())) {
            ++i;
        }
        deck.get(i).setOnHand(false);
    }
}
