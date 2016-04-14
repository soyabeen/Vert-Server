package ch.uzh.ifi.seal.soprafs16.model;

import ch.uzh.ifi.seal.soprafs16.constant.LootType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Defines a loot on train or at player.
 * Created by mirkorichter on 22.03.16.
 */
@Entity
public class Loot implements Positionable, Serializable {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * Defines type of this loot.
     */
    @Column(nullable = false)
    private LootType type;

    /**
     * Defines value of this loot.
     */
    @Column(nullable = false)
    private int value;

    /**
     * The id of the game to which the loot belongs.
     */
    @Column(nullable = false)
    private Long gameId;

    /**
     * Train car level on which loot is.
     */
    @Column(nullable = false)
    private Positionable.Level level;

    /**
     * Car on which loot is.
     */
    @Column
    private int car;

    protected Loot() {
    }

    public Loot(LootType type, Long gameId, int value, int car, Level level) {
        this.type = type;
        this.gameId = gameId;
        this.value = value;
        this.car = car;
        this.level = level;
    }

    @Override
    public String toString() {
        return "Loot (id:"+id+", type:"+type+", value:"+value+", car:"+car+", level:"+level+")";
    }

    /**
     * @return int The current car of the train for the positionable object.
     */
    @Override
    public int getCar() {
        return car;
    }

    /**
     * @return Level The level of the car for llot.
     */
    @Override
    public Level getLevel() {
        return level;
    }

    /**
     * Moves loot n cars in the direction of the locomotive.
     *
     * @param nrOfCarsToMove Number of cars to move.
     */
    @Override
    public void moveToHead(int nrOfCarsToMove) {
        car -= nrOfCarsToMove;
    }

    /**
     * Moves loot n cars in the direction of the trains tail.
     *
     * @param nrOfCarsToMove Number of cars to move.
     */
    @Override
    public void moveToTail(int nrOfCarsToMove) {
        car += nrOfCarsToMove;
    }

    /**
     * Sets type of this loot.
     *
     * @param type type of this loot.
     */
    public void setType(LootType type) {
        this.type = type;
    }

    /**
     * Returns type of loot.
     *
     * @return type of loot.
     */
    public LootType getType() {

        return type;
    }

    /**
     * Sets value of this loot.
     *
     * @param value value of this loot.
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Returns value of this loot.
     *
     * @return value of this loot.
     */
    public int getValue() {

        return value;
    }
}
