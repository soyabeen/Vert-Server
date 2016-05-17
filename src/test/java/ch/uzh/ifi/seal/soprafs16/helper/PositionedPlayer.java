package ch.uzh.ifi.seal.soprafs16.helper;

import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;

/**
 * Created by soyabeen on 22.04.16.
 */
public class PositionedPlayer {

    public static Builder builder() {
        return new PositionedPlayer.Builder();
    }

    public static class Builder {
        private Long id;
        private String username;
        private Positionable.Level level;
        private int car;

        public Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder withUserName(String username) {
            this.username = username;
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

        public Player build() {
            Player p = new Player();
            p.setId(id);
            p.setUsername(username);
            p.setLevel(level);
            p.setCar(car);
            return p;
        }
    }


}
