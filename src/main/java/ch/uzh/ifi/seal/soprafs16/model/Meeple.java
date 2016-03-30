package ch.uzh.ifi.seal.soprafs16.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Defines the state of a Meeple.
 * Created by alexanderhofmann on 22/03/16.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Meeple implements Positionable, Serializable {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * Horizontal position of the meeple.
     */
    @Column
    private int car;

    /**
     * Vertical position on top or inside of a car of the meeple.
     */
    @Column
    private Positionable.Level level;

    public Long getId() {
        return id;
    }

    /**
     * Sets the horizontal position of the meeple.
     * @param car The number of the car starting from the head.
     */
    public void setCar(int car) {
        this.car = car;
    }

    /**
     * Sets the vertical position of the meeple.
     * @param level The level {@link Positionable.Level}
     */
    public void setLevel(Level level) {
        this.level = level;
    }

    @Override
    public int getCar() {
        return car;
    }

    @Override
    public Level getLevel() {
        return level;
    }

    @Override
    public void moveToHead(int nrOfCarsToMove) {
        if (nrOfCarsToMove > car) {
            car = 0;
        }
        car -= nrOfCarsToMove;
    }

    @Override
    public void moveToTail(int nrOfCarsToMove) {
        car += nrOfCarsToMove;
    }
}
