package ch.uzh.ifi.seal.soprafs16.model;

import java.util.logging.Level;

/**
 * Defines the functionality for positioning an object on the game board (train).
 * Created by soyabeen on 22.03.16.
 */
public interface Positionable {

    /**
     * Train car level.
     */
    enum Level {
        BOTTOM, TOP
    }

    /**
     * @return int The current car of the train for the positionable object.
     */
    public int getCar();

    /**
     * @return Level The level of the car for the positionable object.
     */
    public Level getLevel();

    /**
     * Moves the positionable object n cars in the direction of the locomotive.
     * @param nrOfCarsToMove Number of cars to move.
     */
    public void moveToHead(int nrOfCarsToMove);

    /**
     * Moves the positionable object n cars in the direction of the trains tail.
     * @param nrOfCarsToMove Number of cars to move.
     */
    public void moveToTail(int nrOfCarsToMove);

}
