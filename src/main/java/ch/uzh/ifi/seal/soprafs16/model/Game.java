package ch.uzh.ifi.seal.soprafs16.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;

@Entity
public class Game implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable = false) 
	private String name;
	
	@Column(nullable = false) 
	private String owner;
	
	@Column 
	private GameStatus status;
	
	@Column 
	private Integer currentPlayer;

    @OneToMany(mappedBy="game")
    private List<Move> moves;
    
    @ManyToMany(mappedBy="games")
    private List<User> users;

	public Game() {
		this.users = new ArrayList<>();
		this.moves = new LinkedList<>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public List<Move> getMoves() {
		return moves;
	}

	public void setMoves(List<Move> moves) {
		this.moves = moves;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public GameStatus getStatus() {
		return status;
	}

	public void setStatus(GameStatus status) {
		this.status = status;
	}

	public Integer getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Integer currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
   
	public User getNextPlayer() {
		return getUsers().get((getCurrentPlayer() + 1) % getUsers().size());
	}

	public void addUser(User user) {
		users.add(user);
	}
}
