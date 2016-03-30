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

    @Column
    private Character character;

    @Column
    private int bullets;

    @Column
    private CardDeck deck;
    
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
        bullets = MAX_BULLETS;

    }

    /**
     * @param loot Loot to add to this player.
     */
    public Player(Loot loot) {
        this();
        loots.add(loot);
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
     * @param loot Loot to pick.
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

    //TODO: add equals method (or implement comparable)


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
}
