package ch.uzh.ifi.seal.soprafs16.model;

import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    private Long currentPlayerId;

    @Column
    private int nrOfCars;

    @Column
    private int roundId;

    @Column
    private int positionMarshal;

    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @OrderBy("id")
    private List<Player> players;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "gameId")
    private List<Loot> loots;

    @Column
    private int turnId;

    public Game() {
        this.players = new ArrayList<>();
        this.loots = new ArrayList<>();
        turnId = 1;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", nrOfCars=" + nrOfCars +
                ", numberOfPlayers=" + numberOfPlayers +
                ", status=" + status +
                ", currentPlayerId=" + currentPlayerId +
                ", loots=" + loots +
                ", players=" + players +
                '}';
    }


    public void addLoot(Loot loot) {
        loots.add(loot);
    }

    public List<Loot> getLoots() {
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


    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public Long getCurrentPlayerId() {
        return currentPlayerId;
    }

    public void setCurrentPlayerId(Long currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public int getNrOfCars() {
        return nrOfCars;
    }

    public void setNrOfCars(int nrOfCars) {
        this.nrOfCars = nrOfCars;
    }

    public void setLoots(List<Loot> loots) {
        this.loots = loots;
    }

    public int getRoundId() {return roundId;}

    public void setRoundId(int roundId) {this.roundId = roundId;}

    public void incrementRound() {roundId++;}

    public int getTurnId() {return turnId;}

    public void setTurnId(int turnId) {this.turnId = turnId;}

    public int getPositionMarshal() {return positionMarshal;}

    public void setPositionMarshal(int positionMarshal) {this.positionMarshal = positionMarshal;}
}