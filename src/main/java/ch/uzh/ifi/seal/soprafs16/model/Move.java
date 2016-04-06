package ch.uzh.ifi.seal.soprafs16.model;

import ch.uzh.ifi.seal.soprafs16.exception.InvalidInputException;

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
	private boolean pass;

	public Move() {
		this.pass = false;
	}

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
		if(playedCard != null) {
			this.playedCard = playedCard;
		} else throw new InvalidInputException();
	}

	/**
	 * Gives back the information if the player wants to pass the round.
	 * @return pass
     */
	public boolean isPass() { return this.pass; }

	/**
	 * Set variable to true if player doesn't want to pass a card in his round.
	 * @param pass
     */
	public void setPass(boolean pass) {
		this.pass = pass;
	}
}
