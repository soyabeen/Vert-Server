package ch.uzh.ifi.seal.soprafs16.engine.rule;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.lang.reflect.InvocationTargetException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Defines the set of rule needed for a certain card/activity to calculate the possibilities
 * and to execute the actual chosen action.
 * <p>
 * Created by soyabeen on 19.04.16.
 */
public abstract class RuleSet {

    private static final Map<CardType, Class> mapping = initMapping();

    private static Map<CardType, Class> initMapping() {
        Map<CardType, Class> cardMapping = new EnumMap<>(CardType.class);
        cardMapping.put(CardType.MOVE, MoveRuleSet.class);
        cardMapping.put(CardType.FIRE, ShootRuleSet.class);
        cardMapping.put(CardType.FLOORCHANGE, FloorChangeRuleSet.class);
        return cardMapping;
    }

    /**
     * Creates a new instance of the RuleSet implementation corresponding to the given card type.
     *
     * @param type CardType
     * @return Instance of the corresponding RuleSet implementation.
     * @throws InvocationTargetException If the instance could not be created.
     * @throws IllegalArgumentException  If no corresponding RuleSet implementation for the given type could be found.
     */
    public static RuleSet createRuleSet(CardType type) throws InvocationTargetException {
        if (mapping.containsKey(type)) {
            try {
                return (RuleSet) mapping.get(type).getConstructor().newInstance();
            } catch (Exception e) {
                throw new InvocationTargetException(e);
            }
        }
        throw new IllegalArgumentException("Could not create RuleSet for unknown type " + type.toString());
    }

    /**
     * Calculates the possible target objects and outcomes for a card/activity.
     *
     * @return Possibilities The positionable objects that are possible targets for this card.
     */
    public abstract List<Positionable> simulate(Game game, Player player);

    public abstract List<Positionable> execute(ActionCommand command);

}
