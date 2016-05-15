package ch.uzh.ifi.seal.soprafs16.dto;

import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs16.model.Card;
import ch.uzh.ifi.seal.soprafs16.model.Loot;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by soyabeen on 02.05.16.
 */
public class GameWithLastPlayedCardDTO implements Serializable {

    private Long id;
    private String name;
    private String owner;
    private int numberOfPlayers;
    private Long currentPlayerId;
    private int roundId;
    private int turnId;
    private int nrOfCars;
    private GameStatus status;
    private Card lastPlayedCard;
    private List<Player> players;
    private List<Loot> loots;

    @Override
    public String toString() {
        return "GameWithLastPlayedCardDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", numberOfPlayers=" + numberOfPlayers +
                ", currentPlayerId=" + currentPlayerId +
                ", roundId=" + roundId +
                ", turnId=" + turnId +
                ", nrOfCars=" + nrOfCars +
                ", status=" + status +
                ", lastPlayedCard=" + lastPlayedCard +
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

    public Card getLastPlayedCard() {
        return lastPlayedCard;
    }

    public void setLastPlayedCard(Card lastPlayedCard) {
        this.lastPlayedCard = lastPlayedCard;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Loot> getLoots() {
        List<Loot> lootsOnTrain = new ArrayList<>();
        for(Loot l: loots) {
            if (l.getOwnerId() == null) {
                lootsOnTrain.add(l);
            }
        }
        return lootsOnTrain;
    }

    public void setLoots(List<Loot> loots) {
        this.loots = loots;
    }

    public int getTurnId() {return turnId;}

    public void setTurnId(int turnId) {this.turnId = turnId;}
}
