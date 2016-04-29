package ch.uzh.ifi.seal.soprafs16.engine;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.engine.rule.RuleSet;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.lang.reflect.InvocationTargetException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * This engine overs a simple way to process the action phase.
 * The engine handles the game rules/constraints and their correct execution.
 * <p>
 * Created by soyabeen on 20.04.16.
 */
public class GameEngine {

    private Map<CardType, RuleSet> mappingStore;

    public GameEngine() {
        mappingStore = new EnumMap<>(CardType.class);
    }

    /**
     * Returns the rule set for the given card. If the needed rule set is not in the store, a new rule set will be created.
     *
     * @param cardType Type of card.
     * @return Corresponding RuleSet for the given card.
     * @throws InvocationTargetException If the rule set could not be created.
     */
    private RuleSet getRuleSetForCardType(CardType cardType) throws InvocationTargetException {
        if (!mappingStore.containsKey(cardType)) {
            mappingStore.put(cardType, RuleSet.createRuleSet(cardType));
        }
        return mappingStore.get(cardType);
    }


    public List<Positionable> simulateAction(ActionCommand commandInfo) throws InvocationTargetException {
        return getRuleSetForCardType(commandInfo.getCard()).simulate(commandInfo.getGame(), commandInfo.getCurrentPlayer());
    }

    public List<Positionable> executeAction(ActionCommand commandInfo) throws InvocationTargetException {
        RuleSet rs = getRuleSetForCardType(commandInfo.getCard());
        return rs.execute(commandInfo);
    }
}
