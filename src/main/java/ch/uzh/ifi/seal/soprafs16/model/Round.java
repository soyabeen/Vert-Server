package ch.uzh.ifi.seal.soprafs16.model;

import ch.uzh.ifi.seal.soprafs16.constant.RoundEndEvent;
import ch.uzh.ifi.seal.soprafs16.constant.Turn;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Stack;

/**
 * This class represents a round card as well as a train station card.
 * It describes the structure (nr of turns, typ of turns, end events) of a round.
 * <p>
 * Created by soyabeen on 22.03.16.
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"GAME_ID", "POSITION"})})
public class Round implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, name = "GAME_ID")
    private Game game;

    @Column(nullable = false, name = "POSITION")
    private Integer nthRound;

    @OneToMany
    private List<Card> cardStack;

    @ElementCollection(targetClass = Turn.class)
    @CollectionTable(name = "round_turn")
    @Column(name = "turns")
    private List<Turn> turns;

    @Enumerated(EnumType.STRING)
    private RoundEndEvent end;

    protected Round() {
    }

    /**
     * Create a new Round event with the given configuration.
     *
     * @param game     The game to which the round belongs.
     * @param turns    The turns for that round.
     * @param endEvent The end event.
     */
    public Round(Game game, Integer nthRound, List<Turn> turns, RoundEndEvent endEvent) {
        this.game = game;
        this.nthRound = nthRound;
        this.turns = turns;
        this.end = endEvent;
        this.cardStack = new Stack<>();
    }

    @Override
    public String toString() {
        return "Round{" +
                "game=" + game.getId() +
                ", turns=" + turns +
                ", end=" + end +
                '}';
    }

    public void executeActionPhase() {

        throw new IllegalStateException("Method not yet implemented!");

        // increment nthRound
        //this.incrementNthRound();
    }

    /**
     * Add a new card to the card stack.
     *
     * @param playedCard Played card from a player.
     */
    public void addNewlyPlayedCard(Card playedCard) {
        cardStack.add(playedCard);
    }

    public Game getGame() {
        return game;
    }

    public List<Card> getCardStack() {
        return cardStack;
    }

    public List<Turn> getTurns() {
        return turns;
    }

    public void setTurns(List<Turn> turns) {
        this.turns = turns;
    }

    public Integer getNthRound() {
        return nthRound;
    }

    public void incrementNthRound() {
        this.nthRound++;
    }
}
