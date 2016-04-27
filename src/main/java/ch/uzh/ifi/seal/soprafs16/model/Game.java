package ch.uzh.ifi.seal.soprafs16.model;

import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs16.utils.InputArgValidator;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
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
    private Long currentPlayerId;

    @Column
    private int nrOfCars;

    @Column
    private int roundId;

    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @OrderBy("id")
    private List<Player> players;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Loot> loots;


    public Game() {
        this.players = new ArrayList<>();
        this.loots = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Game{" +
                "loots=" + loots +
                ", players=" + players +
                ", nrOfCars=" + nrOfCars +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", numberOfPlayers=" + numberOfPlayers +
                ", status=" + status +
                ", currentPlayerId=" + currentPlayerId +
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
}