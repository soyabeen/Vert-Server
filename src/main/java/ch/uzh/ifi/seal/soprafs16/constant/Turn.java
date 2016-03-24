package ch.uzh.ifi.seal.soprafs16.constant;

import ch.uzh.ifi.seal.soprafs16.model.Modified;

/**
 * Types of a turn in a round.
 * <p/>
 * Created by soyabeen on 24.03.16.
 */
public enum Turn implements Modified {
    NORMAL {
        @Override
        public void handleRoundModification() {
            throw new IllegalStateException("Method not yet implemented!");
        }
    },
    HIDDEN {
        @Override
        public void handleRoundModification() {
            throw new IllegalStateException("Method not yet implemented!");
        }
    }, DOUBLE_TURNS {
        @Override
        public void handleRoundModification() {
            throw new IllegalStateException("Method not yet implemented!");
        }
    }, REVERSE {
        @Override
        public void handleRoundModification() {
            throw new IllegalStateException("Method not yet implemented!");
        }
    }
}

