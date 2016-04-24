package ch.uzh.ifi.seal.soprafs16.dto;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mirkorichter on 24.04.16.
 */
public class TurnDTO {

    private CardType type;

    //Future Meeple destinations
    private List<Player> players;

    private boolean punchRight;

    private int lootID;


    public TurnDTO() {
        players = new ArrayList<Player>();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public CardType getType() {

        return type;
    }

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
}
