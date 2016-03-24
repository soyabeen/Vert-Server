package ch.uzh.ifi.seal.soprafs16.model;

import ch.uzh.ifi.seal.soprafs16.constant.Ability;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines the state of a playing character.
 * Created by alexanderhofmann on 22/03/16.
 */
@Entity
public class Player extends Meeple implements Serializable {

    /**
     * Bullets the player has left.
     */
    @Column
    private int bullets;

    /**
     * List of loots.
     */
    @Column
    private List<Loot> loots;

    /**
     * Player's special ability.
     */
    @Column
    private Ability ability;

    /**
     * Player's deck.
     */
    @Column
    private CardDeck deck;

    /**
     * Player's hand.
     */
    @Column
    private List<Card> hand;

    /**
     * Default constructor.
     */
    public Player() {
        this.loots = new ArrayList<Loot>();
        this.hand = new ArrayList<Card>();
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
     * Gets the player's special ability.
     * @return player's ability.
     */
    public Ability getAbility() {
        return ability;
    }

    /**
     * Sets the player's special ability.
     * @param ability Player's special ability.
     */
    public void setAbility(Ability ability) {
        this.ability = ability;
    }
}
