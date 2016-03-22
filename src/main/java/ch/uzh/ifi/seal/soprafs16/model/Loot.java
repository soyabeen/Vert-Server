package ch.uzh.ifi.seal.soprafs16.model;

/**
 * Created by mirkorichter on 22.03.16.
 */
public class Loot {

    private LootType type;

    private int value;

    public void setType(LootType type) {
        this.type = type;
    }

    public LootType getType() {

        return type;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {

        return value;
    }




}
