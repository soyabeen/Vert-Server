package ch.uzh.ifi.seal.soprafs16.model;

/**
 * Created by mirkorichter on 08.05.16.
 */
public class Marshal extends Meeple {

    public Marshal() {}

    public Marshal(int car) {
        this.setCar(car);
    }

    public void update(Marshal m) {
        this.setCar(m.getCar());
    }
}
