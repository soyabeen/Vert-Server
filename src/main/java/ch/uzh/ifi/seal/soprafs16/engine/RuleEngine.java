package ch.uzh.ifi.seal.soprafs16.engine;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.engine.rule.RuleSet;
import ch.uzh.ifi.seal.soprafs16.model.Card;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

/**
 * This engine overs a simple way to process the action phase.
 * The engine handles the game rules/constraints and their correct execution.
 * <p>
 * Created by soyabeen on 20.04.16.
 */
public class RuleEngine {

    private static final Logger logger = LoggerFactory.getLogger(RuleEngine.class);

    private HashMap<CardType, RuleSet> mappingStore;

    public RuleEngine() {
        mappingStore = new HashMap<>();
    }

    /**
     * Returns the rule set for the given card. If the needed rule set is not in the store, a new rule set will be created.
     *
     * @param card
     * @return Corresponding RuleSet for the given card.
     * @throws InvocationTargetException If the rule set could not be created.
     */
    private RuleSet getRuleSetForCard(Card card) throws InvocationTargetException {
        if (!mappingStore.containsKey(card.getType())) {
            mappingStore.put(card.getType(), RuleSet.createRuleSet(card.getType()));
        }
        return mappingStore.get(card.getType());
    }



    public List<Positionable> simulatePlayedCard(Game game, Card playedCard, Player player) throws InvocationTargetException {
        return getRuleSetForCard(playedCard).simulate(game, player);
    }
}
