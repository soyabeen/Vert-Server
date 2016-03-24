package ch.uzh.ifi.seal.soprafs16.model;

import ch.uzh.ifi.seal.soprafs16.constant.RoundEndEvent;
import ch.uzh.ifi.seal.soprafs16.constant.Turn;

import javax.persistence.*;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * This class represents a round card as well as a train station card.
 * It describes the structure (nr of turns, typ of turns, end events) of a round.
 * <p/>
 * Created by soyabeen on 22.03.16.
 */
@Entity
public class Round implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Game game;

    @OneToMany
    private LinkedList<Card> cardStack;

    @ElementCollection(targetClass=Turn.class)
    @CollectionTable(name="round_turn")
    @Column(name="turns")
    private LinkedList<Turn> turns;

    @Enumerated(EnumType.STRING)
    private RoundEndEvent end;

    public Round() {
    }

    /**
     * Create a new Round event with the given configuration.
     *
     * @param game The game to which the round belongs.
     * @param turns The turns for that round.
     * @param endEvent The end event.
     */
    public Round(Game game, LinkedList<Turn> turns, RoundEndEvent endEvent) {
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

}
