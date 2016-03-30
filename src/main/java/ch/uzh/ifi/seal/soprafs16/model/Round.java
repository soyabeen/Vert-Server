package ch.uzh.ifi.seal.soprafs16.model;

import ch.uzh.ifi.seal.soprafs16.constant.RoundEndEvent;
import ch.uzh.ifi.seal.soprafs16.constant.Turn;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * This class represents a round card as well as a train station card.
 * It describes the structure (nr of turns, typ of turns, end events) of a round.
 * <p/>
 * Created by soyabeen on 22.03.16.
 */
@Entity
public class Round implements Serializable {

    private static final long serialVersionUID = 1L;
    private static Integer totalMadeMoves = 0;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Game game;

    @Column
    private Integer nthRound;

    @OneToMany
    private List<Card> cardStack;

    @ElementCollection(targetClass=Turn.class)
    @CollectionTable(name="round_turn")
    @Column(name="turns")
    private List<Turn> turns;

    @Enumerated(EnumType.STRING)
    private RoundEndEvent end;

    protected Round() {
    }

    /**
     * Create a new Round event with the given configuration.
     *
     * @param game The game to which the round belongs.
     * @param turns The turns for that round.
     * @param endEvent The end event.
     */
    public Round(Game game, Integer nthRound, List<Turn> turns, RoundEndEvent endEvent) {
        this.game = game;
        this.nthRound = nthRound;
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
        totalMadeMoves++;
    }

    /**
     * Pass this turn and take up 3 cards.
     */
    public void passAndTake3Cards() {
        totalMadeMoves++;
    }

    public Game getGame() {
        return game;
    }

    public List<Card> getCardStack() {
        return cardStack;
    }

    public List<Turn> getTurns() { return turns; }

    public void setTurns(List<Turn> turns) { this.turns = turns; }

    public Integer getNthRound() { return nthRound; }

    public void setNthRound(Integer nthRound) { this.nthRound = nthRound; }

    public Integer getTotalMadeMoves() { return totalMadeMoves; }
}
