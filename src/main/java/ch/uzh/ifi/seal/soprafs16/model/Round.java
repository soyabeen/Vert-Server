package ch.uzh.ifi.seal.soprafs16.model;

import org.hibernate.cfg.NotYetImplementedException;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.LinkedList;

/**
 * This class represents a round card as well as a train station card.
 * It describes the structure (nr of turns, typ of turns, end events) of a round.
 * <p/>
 * Created by soyabeen on 22.03.16.
 */
@Entity
public class Round {

    // Game to which the round belongs.
    @ManyToOne
    private Game game;

    @OneToOne
    private LinkedList<Card> cardStack;

    @OneToMany
    private LinkedList<Turn> turns;

    @OneToMany
    private EndEvent end;

    public Round() {
    }

    /**
     * Create a new Round event with the given configuration.
     *
     * @param game The game to which the round belongs.
     * @param turns The turns for that round.
     * @param endEvent The end event.
     */
    public Round(Game game, LinkedList<Turn> turns, EndEvent endEvent) {
        this.game = game;
        this.turns = turns;
        this.end = endEvent;
    }

    public void executeActionPhase() {
        throw new IllegalStateException("Method not yet implemented!");
    }

    /**
     * Add a new card to the played card stack.
     * @param playedCard Played card from a player.
     */
    public void addNewlyPlayedCard(Card playedCard) {
        cardStack.add(playedCard);
    }

    public Game getGame() {
        return game;
    }

    public LinkedList<Card> getCardStack() {
        return cardStack;
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
