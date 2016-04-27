package ch.uzh.ifi.seal.soprafs16.helper;

import ch.uzh.ifi.seal.soprafs16.constant.LootType;
import ch.uzh.ifi.seal.soprafs16.model.Loot;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;

/**
 * Created by soyabeen on 22.04.16.
 */
public class PositionedLoot {

    public static Builder builder() {
        return new PositionedLoot.Builder();
    }

    public static class Builder {
        private long gameId = 1L;
        private LootType type;
        private Positionable.Level level;
        private int car;

        public Builder() {
        }

        public Builder withType(LootType type) {
            this.type = type;
            return this;
        }

        public Builder onLowerLevelAt(int car) {
            this.car = car;
            this.level = Positionable.Level.BOTTOM;
            return this;
        }

        public Builder onUpperLevelAt(int car) {
            this.car = car;
            this.level = Positionable.Level.TOP;
            return this;
        }

        public Loot build() {
            return new Loot(type, gameId, type.value(), car, level);
        }
    }


}
