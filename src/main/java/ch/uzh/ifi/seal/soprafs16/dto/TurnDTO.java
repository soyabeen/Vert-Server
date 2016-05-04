package ch.uzh.ifi.seal.soprafs16.dto;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.model.Loot;
import ch.uzh.ifi.seal.soprafs16.model.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mirkorichter on 24.04.16.
 */
public class TurnDTO implements Serializable {

    private CardType type;

    //Future Meeple or Loot destinations
    private List<Player> players;

    private List<Loot> loots;

    private boolean punchRight;

    private int lootID;


    public TurnDTO() {
        loots = new ArrayList<>();
        players = new ArrayList<>();
    }

    public TurnDTO(CardType type, List<Player> positionables) {
        this.type = type;
        this.players = positionables;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void addPlayersAsList(List<Player> players) {this.players = players;}

    public CardType getType() { return type; }

    public void setType(CardType type) {
        this.type = type;
    }

    public boolean isPunchRight() {
        return punchRight;
    }

    public void setPunchRight(boolean punchRight) {
        this.punchRight = punchRight;
    }

    public int getLootID() {
        return lootID;
    }

    public void setLootID(int lootID) {
        this.lootID = lootID;
    }

    public List<Loot> getLoots() {
        return loots;
    }

    public void addLoot(Loot loot) {
        loots.add(loot);
    }

    public void addLootsAsList(List<Loot> loot) {
        loots =loot;
    }

}
