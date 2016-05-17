package ch.uzh.ifi.seal.soprafs16.engine.rule.sim;

import ch.uzh.ifi.seal.soprafs16.engine.rule.RuleUtils;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soyabeen on 27.04.16.
 */
public class FloorChangeSimRule implements SimulationRule {

    @Override
    public boolean evaluate(Positionable actor) {
        // Floor change is always possible
        return true;
    }

    @Override
    public List<Positionable> simulate(Positionable actor) {
        List<Positionable> emPlayers = new ArrayList<>();

        Player tmp = (Player) actor;
        Player emulated = new Player();
        emulated.setUsername(tmp.getUsername());
        emulated.setCar(tmp.getCar());
        emulated.setLevel(tmp.getLevel());
        emPlayers.add(RuleUtils.swapLevel(emulated));

        return emPlayers;
    }
}
