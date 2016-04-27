package ch.uzh.ifi.seal.soprafs16.utils;

import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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


    public List<Player> filterPlayersOnSameCar(Player actor, List<Player> players) {
        List<Player> result = new ArrayList<>();
        for (Player target : players) {
            if (actor.getCar() != target.getCar()) {
                result.add(target);
            }
        }
        return result;
    }

    /**
     * Extract the players that are in sight (means smallest car nr.)
     *
     * @param players
     * @return
     */
    private List<Player> extractPlayersInSight(ArrayList<Player> players) {
        ArrayList<Player> extracted = new ArrayList<>();
        int inSight = -1;
        for (Player p : players) {
            logger.debug("Extract: {}", p);
            if (inSight == -1) {
                inSight = p.getCar();
            }
            if (inSight == p.getCar()) {
                extracted.add(p);
            }
        }
        return extracted;
    }

    public List<Player> findTargetToShootOnUpperLevel(Player actor, List<Player> players) {
        List<Player> result = new ArrayList<>();

        List<Player> filtered = filterPlayersOnSameCar(actor, players);
        List<Player> onSameLevel = filterPlayersByLevel(filtered, Positionable.Level.TOP);
        ArrayList<Player> towardsHead = new ArrayList<>();
        ArrayList<Player> towardsTail = new ArrayList<>();

        // Filter target players for position.
        // Target players between the train head and the actor are stored to the head list.
        for (Player p : onSameLevel) {
            if (p.getCar() < actor.getCar()) {
                towardsHead.add(p);
            } else {
                towardsTail.add(p);
            }
        }

        // Get players in sight - direction towards train tail
        Collections.sort(towardsTail, new ByCarOrder());
        result.addAll(extractPlayersInSight(towardsTail));

        // Get players in sight - direction towards train head
        Collections.sort(towardsHead, new ByCarOrder());
        Collections.reverse(towardsHead);
        result.addAll(extractPlayersInSight(towardsHead));

        return result;
    }

    public List<Player> findTargetToShoot(Player actor, List<Player> players) {
        if (Positionable.Level.TOP == actor.getLevel()) {
            return findTargetToShootOnUpperLevel(actor, players);
        } else {
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

    /**
     * Helper class to sort players by car nr.
     */
    private class ByCarOrder implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            Positionable p1 = (Positionable) o1;
            Positionable p2 = (Positionable) o2;
            return p1.getCar() - p2.getCar();
        }

    }
}
