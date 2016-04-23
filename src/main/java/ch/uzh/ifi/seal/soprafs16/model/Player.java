package ch.uzh.ifi.seal.soprafs16.model;

import ch.uzh.ifi.seal.soprafs16.constant.Character;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @Column
    private CardDeck deck;

    @Column
    private Integer totalMadeMoves;

    @OneToMany(mappedBy = "owner")
    private List<Card> hand;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Loot> loots;

    @Transient
    private static final int MAX_BULLETS = 6;

    /**
     * Default constructor.
     */
    public Player() {
        this.loots = new ArrayList<>();
        this.hand = new ArrayList<>();
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

    public Player(Loot loot, CardDeck playerDeck) {
        this(loot);
        this.deck = playerDeck;
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
     * @return bullets left in chamber.
     */
    public int getBullets() {
        return bullets;
    }

    /**
     * Sets the number of bullets.
     * @param bullets number of bullets to set.
     */
    public void setBullets(int bullets) {
        this.bullets = bullets;
    }

    /**
     * Gets a list of loots.
     * @return
     */
    public List<Loot> getLoots() {
        return loots;
    }

    /**
     * Adds a piece of loot.
     * @param loot
     */
    public void addLoot(Loot loot) {
        loots.add(loot);
    }

    /**
     * Drops a piece of loot.
     * @param loot Loot to drop.
     */
    public void dropLoot(Loot loot) {
        loots.remove(loot);
    }

    /**
     * Gets the player's special character.
     * @return player's character.
     */
    public Character getCharacter() {
        return character;
    }

    /**
     * Sets the player's special character.
     * @param character Player's special character.
     */
    public void setCharacter(Character character) {
        this.character = character;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + getId() +
                "character=" + character +
                ", bullets=" + bullets +
                '}';
    }

    /**
     * Gets the players cards in hand.
     * @return Cards which the player is holding.
     */
    public List<Card> getHand() {
        return hand;
    }

    /**
     * Sets the new cards the player is holding.
     * @param hand Updated collection of cards the player is going to hold.
     */
    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    /**
     * Adds 3 Cards from card deck to players hand.
     */
    public void take3Cards() {
        this.hand.addAll( this.deck.drawCard(3) );
    }

    /**
     * Gets the amount of rounds a player made a move.
     * @return totalMadeMoves
     */
    public Integer getTotalMadeMoves() {
        return totalMadeMoves;
    }

    /**
     * Increments the amount of rounds a player made.
     *
     */
    public void incrementTotalMadeMoves() {
        this.totalMadeMoves++;
    }

}
