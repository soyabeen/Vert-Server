package ch.uzh.ifi.seal.soprafs16.constant;

import java.util.Random;

/**
 * All possible types of a loot.
 * Created by mirkorichter on 22.03.16.
 */
public enum LootType {
    JEWEL(500),
    PURSE_SMALL(250),
    PURSE_BIG(500),
    STRONGBOX(1000);

    private final int value;

    LootType(int value) {
        this.value = value;
    }

    public static LootType getRandomPurseType() {
        int rnd = new Random().nextInt();
        if(rnd % 2 == 0) {
            return PURSE_BIG;
        }
        return PURSE_SMALL;
    }

    public int value() {
        return value;
    }
}
