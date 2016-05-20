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
    @JoinColumn(name="PLAYER_ID")
    private Player player;

	@OneToOne
	private Card playedCard;

	@Column
	private boolean pass;

	public Move() {
		this.pass = false;
	}

	@Override
	public String toString() {
		return "Move (id:" + id
				+ ", playedCard:" + playedCard.toString()
				+ ", pass:" + pass
				+ ", game:" + game.toString()
				+ ", player:" + player.toString();
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

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Card getPlayedCard() {
		return playedCard;
	}

	public void setPlayedCard(Card playedCard) {
		if(playedCard != null) {
			this.playedCard = playedCard;
		} else throw new IllegalArgumentException("Played card must not be null!");
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
