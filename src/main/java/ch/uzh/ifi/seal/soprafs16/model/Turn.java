package ch.uzh.ifi.seal.soprafs16.model;



import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a possible outcome of a played Card
 * Created by mirkorichter on 16.04.16.
 */
public class Turn {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToMany
    private List<Player> attackablePlayers;

    @Column
    private boolean directionRight;

    @Column
    private int distance;

    @ManyToMany
    private List<Loot> loots;

    public Turn() { attackablePlayers = new ArrayList<Player>();};

    public List<Player> getAttackablePlayers() {
        return attackablePlayers;
    }

    public void addAttackablePlayer(Player player) {
        attackablePlayers.add(player);
    }

    public boolean isDirectionRight() {
        return directionRight;
    }

    public void setDirectionRight(boolean directionRight) {
        this.directionRight = directionRight;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public List<Loot> getLoots() {
        return loots;
    }

    public void setLoots(List<Loot> loots) {
        this.loots = loots;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
