package ch.uzh.ifi.seal.soprafs16.model;

import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

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
    private int numberOfPlayers;

    @Column
    private GameStatus status;

    @Column
    private int currentPlayer;

    @OneToMany(mappedBy = "game")
    private List<Move> moves;

    @OneToMany(fetch = FetchType.EAGER)
    private List<User> users;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<Loot> loots;


    public Game() {
        this.users = new ArrayList<>();
        this.moves = new LinkedList<>();
        this.loots = new LinkedHashSet<>();
    }

    @Override
    public String toString() {
        return "Game(id=" + id + ", name=" + name
                + ", owner=" + owner + ", nrPlayer=" + numberOfPlayers
                + ", status=" + status + ", currentPlayer=" + currentPlayer + ")";
    }

    public void addLoot(Loot loot) {
        loots.add(loot);
    }

    public Collection<Loot> getLoots() {
        return loots;
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

    public void addUser(User user) {
        users.add(user);
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }
}