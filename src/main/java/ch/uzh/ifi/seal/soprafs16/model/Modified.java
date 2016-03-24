package ch.uzh.ifi.seal.soprafs16.model;

/**
 * Indicates how to handle modified round turns.
 * <p/>
 * Created by soyabeen on 22.03.16.
 */
@FunctionalInterface
public interface Modified {

    /**
     * Handles the special modifications for a turn.
     */
    public void handleRoundModification();

}
