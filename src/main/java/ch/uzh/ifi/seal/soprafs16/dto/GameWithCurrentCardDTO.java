package ch.uzh.ifi.seal.soprafs16.dto;

import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs16.model.Card;
import ch.uzh.ifi.seal.soprafs16.model.Loot;
import ch.uzh.ifi.seal.soprafs16.model.Player;

import java.io.Serializable;
import java.util.List;

/**
 * Created by soyabeen on 02.05.16.
 */
public class GameWithCurrentCardDTO implements Serializable {

    private Long id;
    private String name;
    private String owner;
    private int numberOfPlayers;
    private Long currentPlayerId;
    private int roundId;
    private int turnId;
    private int nrOfCars;
    private GameStatus status;
    private Card currentCard;
    private List<Player> players;
    private List<Loot> loots;
    private int positionMarshal;
    private Long exitPlayerId;

    @Override
    public String toString() {
        return "GameWithCurrentCard{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", numberOfPlayers=" + numberOfPlayers +
                ", currentPlayerId=" + currentPlayerId +
                ", roundId=" + roundId +
                ", turnId=" + turnId +
                ", nrOfCars=" + nrOfCars +
                ", positionMarshal=" + positionMarshal +
                ", status=" + status +
                ", currentCard=" + currentCard +
                ", players=" + players +
                ", loots=" + loots +
                '}';
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

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public Long getCurrentPlayerId() {
        return currentPlayerId;
    }

    public void setCurrentPlayerId(Long currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

    public int getRoundId() {
        return roundId;
    }

    public void setRoundId(int roundId) {
        this.roundId = roundId;
    }

    public int getNrOfCars() {
        return nrOfCars;
    }

    public void setNrOfCars(int nrOfCars) {
        this.nrOfCars = nrOfCars;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public Card getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Loot> getLoots() { return loots; }

    public void setLoots(List<Loot> loots) {
        this.loots = loots;
    }

    public int getTurnId() {return turnId;}

    public void setTurnId(int turnId) {this.turnId = turnId;}

    public int getPositionMarshal() {return positionMarshal;}

    public void setPositionMarshal(int positionMarshal) {this.positionMarshal = positionMarshal;}

    public Long getExitPlayerId() {return exitPlayerId;}

    public void setExitPlayerId(Long exitPlayerId) {this.exitPlayerId = exitPlayerId;}
}
