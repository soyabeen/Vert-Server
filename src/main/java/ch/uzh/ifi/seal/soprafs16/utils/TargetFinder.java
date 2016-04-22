package ch.uzh.ifi.seal.soprafs16.utils;

import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soyabeen on 22.04.16.
 */
public class TargetFinder {

    private static final Logger logger = LoggerFactory.getLogger(TargetFinder.class);


    private List<Player> filterPlayersByLevel(List<Player> players, Positionable.Level level) {
        List<Player> result = new ArrayList<>();
        for (Player player : players) {
            if (level == player.getLevel()) {
                logger.debug("Player {} on level {}.", player.toString(), level);
                result.add(player);
            }
        }
        return result;
    }

    private int calculateDistance(Player p1, Player p2) {
        return Math.abs(p1.getCar() - p2.getCar());
    }

    private boolean isInAllowedRange(Player current, Player target, int range) {
        int distance = calculateDistance(current, target);
        boolean res = distance <= range;
        if (!res) {
            logger.debug("Not in allowed range ({}): {}, {}", range, current.toString(), target.toString());
        }
        return res;
    }

    private boolean standsInNeighboringCar(Player actor, Player target) {
        return isInAllowedRange(actor, target, 1);
    }


    public List<Player> findTargetToShootOnLowerLevel(Player actor, List<Player> players) {
        List<Player> result = new ArrayList<>();
        for (Player pos : filterPlayersByLevel(players, Positionable.Level.BOTTOM)) {
            if (!actor.getUsername().equals(pos.getUsername())) {
                if (actor.getCar() != pos.getCar()) {
                    if (standsInNeighboringCar(actor, pos)) {
                        result.add(pos);
                    }
                }
            }
        }
        return result;
    }

    public List<Player> findTargetToShootOnUpperLevel(Player actor, List<Player> players) {
        List<Player> result = new ArrayList<>();
        int negDiff = 100;
        int posDiff = 100;
        List<Player> left = new ArrayList<>();
        List<Player> right = new ArrayList<>();
        for (Player pos : filterPlayersByLevel(players, Positionable.Level.TOP)) {
            if (!actor.getUsername().equals(pos.getUsername())) {
                if (actor.getCar() != pos.getCar()) {
                    int distance = calculateDistance(actor, pos);
                    if (actor.getCar() - pos.getCar() < 0) {
                        if (distance < negDiff) {
                            left.clear();
                            left.add(pos);
                            negDiff = distance;
                        }
                        if (distance == negDiff) {
                            left.add(pos);
                        }
                    } else {
                        if (distance < posDiff) {
                            right.clear();
                            right.add(pos);
                            posDiff = distance;
                        }
                        if (distance == posDiff) {
                            right.add(pos);
                        }
                    }
                }
            }
        }
        result.addAll(right);
        result.addAll(left);
        return result;
    }

    public List<Player> findTargetToShoot(Player actor, List<Player> players) {
        if(Positionable.Level.TOP == actor.getLevel()) {
            return findTargetToShootOnUpperLevel(actor, players);
        }else{
            return findTargetToShootOnLowerLevel(actor, players);
        }
    }

    public List<Player> findTargetToPunch(Player actor, List<Player> players) {
        List<Player> result = new ArrayList<>();
        for (Player pos : players) {
            if (actor.getCharacter() != pos.getCharacter()) {
                if (actor.getCar() == pos.getCar()) {
                    if (actor.getLevel() == pos.getLevel()) {
                        result.add(pos);
                    }
                }
            }
        }
        return result;
    }
}
