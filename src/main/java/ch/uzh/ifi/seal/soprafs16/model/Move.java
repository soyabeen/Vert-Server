package ch.uzh.ifi.seal.soprafs16.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Move implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long id;
	
    @ManyToOne
    @JoinColumn(name="GAME_ID")
    private Game game;
    
    @ManyToOne
    @JoinColumn(name="USER_ID")
    private User user;

	@OneToOne
	private Card playedCard;

	@Column
	private Integer nthMove;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Card getPlayedCard() {
		return playedCard;
	}

	public void setPlayedCard(Card playedCard) {
		this.playedCard = playedCard;
	}

	public Integer getNthMove() {
		return nthMove;
	}

	public void setNthMove(Integer nthMove) {
		this.nthMove = nthMove;
	}
}
