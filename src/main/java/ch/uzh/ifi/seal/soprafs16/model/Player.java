package ch.uzh.ifi.seal.soprafs16.model;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.constant.Character;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.eclipse.persistence.annotations.VariableOneToOne;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Defines the state of a playing character.
 * Created by alexanderhofmann on 22/03/16.
 */
@Entity
public class Player extends Meeple {

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String token;

    @Column
    private Character character;

    @Column
    private int bullets;

    @OneToMany(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
    private List<Card> deck;

    @Column
    private Integer totalMadeMoves;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Loot> loots;

    @Transient
    private static final int MAX_BULLETS = 6;

    /**
     * Default constructor.
     */
    public Player() {
        this.loots = new ArrayList<>();
        totalMadeMoves = 0;
        bullets = MAX_BULLETS;
    }

    public Player(String username) {
        this();
        this.username = username;
    }

    /**
     * @param loot Loot to add to this player.
     */
    public Player(Loot loot) {
        this();
        loots.add(loot);
    }

    public Player(Loot loot, List<Card> deck) {
        this(loot);
        this.deck = deck;
    }

    /**
     * Player shoots, reduce the nr of bullets by one.
     *
     * @return The nr of bullets left.
     */
    public int shoots() {
        if (bullets > 0) {
            bullets = bullets - 1;
        }
        return bullets;
    }

    /**
     * Player gets shot, add a bullet card to deck.
     */
    public void getsShot() {
        deck.add(new Card(CardType.BULLET, this.getId()));
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Gets the bullets the player has left.
     *
     * @return bullets left in chamber.
     */
    public int getBullets() {
        return bullets;
    }

    /**
     * Sets the number of bullets.
     *
     * @param bullets number of bullets to set.
     */
    public void setBullets(int bullets) {
        this.bullets = bullets;
    }

    /**
     * Gets a list of loots.
     *
     * @return
     */
    public List<Loot> getLoots() {
        return loots;
    }

    /**
     * Adds a piece of loot.
     *
     * @param loot
     */
    public void addLoot(Loot loot) {
        loots.add(loot);
    }

    /**
     * Drops a piece of loot.
     *
     * @param loot Loot to drop.
     */
    public void dropLoot(Loot loot) {
        loots.remove(loot);
    }

    /**
     * Gets the player's special character.
     *
     * @return player's character.
     */
    public Character getCharacter() {
        return character;
    }

    /**
     * Sets the player's special character.
     *
     * @param character Player's special character.
     */
    public void setCharacter(Character character) {
        this.character = character;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + getId() +
                ", username=" + username +
                ", character=" + character +
                ", bullets=" + bullets +
                ", pos=" + getCar() + "/" + getLevel() +
                '}';
    }

    /**
     * Gets the amount of rounds a player made a move.
     *
     * @return totalMadeMoves
     */
    public Integer getTotalMadeMoves() {
        return totalMadeMoves;
    }

    /**
     * Increments the amount of rounds a player made.
     */
    public void incrementTotalMadeMoves() {
        this.totalMadeMoves++;
    }

    public List<Card> getDeck() {
        return deck;
    }

    public void setDeck(List<Card> deck) {
        this.deck = deck;
    }

    /**
     * Gets the players cards in hand.
     *
     * @return Cards which the player is holding.
     */
    @JsonIgnore
    public List<Card> getCardsOnHand() {
        List<Card> onHand = new ArrayList<>();
        for(Card c: deck) {
            if(c.isOnHand()) onHand.add(c);
        }
        return onHand;
    }

    /**
     * Adds 3 or rest Cards from card deck to players hand.
     */
    public void take3Cards() { this.drawCard(3);
    }

    public void removeCardFromHand(Card card) {
        int i = 0;
        while(!(deck.get(i).getType().equals(card.getType()) && deck.get(i).isOnHand())) {
            ++i;
        }
        deck.get(i).setOnHand(false);
    }

    /**
     * Draws card to start each round
     */
    public void drawHandForStart(){
        for(Card c: deck) c.setOnHand(false);
        if(this.character.equals(Character.DOC)) drawCard(7);
        else drawCard(6);
    }

    /**
     * Draws n many cards form deck randomly.
     * @param numOfCards defines how many cards to draw.
     * @return returns ArrayList of cards on hand of player.
     */
    private void drawCard(int numOfCards) {
        List<Card> currentDeck = getCardsInDeck();

        numOfCards = (numOfCards > currentDeck.size()) ? currentDeck.size() : numOfCards;

        Collections.shuffle(currentDeck);

        for(int i = 0; i < numOfCards; i++) {
            currentDeck.get(i).setOnHand(true);
        }
    }

    private List<Card> getCardsInDeck() {
        List<Card> currentDeck = new ArrayList<>();
        for (Card c: deck) {
            if(!c.isOnHand()) currentDeck.add(c);
        }
        return currentDeck;
    }


}
