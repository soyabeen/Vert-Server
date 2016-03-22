package ch.uzh.ifi.seal.soprafs16.model;

import org.hibernate.cfg.NotYetImplementedException;

import java.util.LinkedList;

/**
 * This class represents a round card as well as a train station card.
 * It describes the structure (nr of turns, typ of turns, end events) of a round.
 * <p/>
 * Created by soyabeen on 22.03.16.
 */
public class Round {

    private LinkedList<Turn> turns;
    private EndEvent end;

    public Round(LinkedList<Turn> turns, EndEvent end) {
        this.turns = turns;
        this.end = end;
    }

    public void executeActionPhase() {
        throw new IllegalStateException("Method not yet implemented!");
    }

    /**
     * Types of a turn in a round.
     */
    enum Turn implements Modified {
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

    /**
     * Possible round end events.
     */
    enum EndEvent {
        ANGRY_MARSHAL, PIVOTTABLE_POLE, BRAKING, GET_IT_ALL, REBELLION, PICKPOCKETING, MARSHALS_REVENGE, HOSTAGE
    }

}
