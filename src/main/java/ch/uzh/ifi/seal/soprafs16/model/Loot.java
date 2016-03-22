package ch.uzh.ifi.seal.soprafs16.model;

/**
 * Defines a loot on train or at player.
 * Created by mirkorichter on 22.03.16.
 */
public class Loot {

    /**
     * Defines type of this loot.
     */
    private LootType type;

    /**
     * Defines value of this loot.
     */
    private int value;

    /**
     * Sets type of this loot.
     * @param type type of this loot.
     */
    public void setType(LootType type) {
        this.type = type;
    }

    /**
     * Returns type of loot.
     * @return type of loot.
     */
    public LootType getType() {

        return type;
    }

    /**
     * Sets value of this loot.
     * @param value value of this loot.
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Returns value of this loot.
     * @return value of this loot.
     */
    public int getValue() {

        return value;
    }




}
