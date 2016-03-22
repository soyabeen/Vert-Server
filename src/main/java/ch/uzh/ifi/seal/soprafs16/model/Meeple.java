package ch.uzh.ifi.seal.soprafs16.model;

/**
 * Defines the state of a Meeple.
 * Created by alexanderhofmann on 22/03/16.
 */
public abstract class Meeple implements Positionable {

    /**
     * Owner of the meeple.
     */
    private User owner;

    /**
     * Horizontal position of the meeple.
     */
    private int car;

    /**
     * Vertical position on top or inside of a car of the meeple.
     */
    private Positionable.Level level;

    /**
     * Returns the owner.
     * @return owner The owner of the meeple.
     */
    public User getOwner() {
        return owner;
    }

    /**
     * Sets the owner of a meeple.
     * @param owner The owner of the meeple.
     */
    public void setOwner(User owner) {
        this.owner = owner;
    }

    /**
     * Sets the horizontal position of the meeple.
     * @param car The number of the car starting from the tail.
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
        car += nrOfCarsToMove;
    }

    @Override
    public void moveToTail(int nrOfCarsToMove) {
        if (nrOfCarsToMove > car) {
            car = 0;
        } else {
            car -= nrOfCarsToMove;
        }

    }
}
