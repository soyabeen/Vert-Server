package engine.rules;


import engine.cards.Card;

/**
 * Created by alex on 19.03.2016.
 */
public interface RuleEngineFactory {

    public static RuleEngineFactory create(Card card) {
        switch (card.getType()) {
            case MOVE:
                return new MoveRuleEngine(card.getOwner());
            case SHOOT:
                return new ShootRuleEngine(card.getOwner());
            case PUNCH:
                return new PunchRuleEngine(card.getOwner());
            default:
                return null;
        }
    }


    public abstract void execute();

    public abstract boolean isValid();
}
