package engine.cards;

import engine.User;
/**
 * Created by alex on 19.03.2016.
 */
public class CardFactory {

    private CardFactory() {

    }

    public static Card createCard(CardType type, User owner) {
        switch (type) {
            case MOVE:
                return new Move(owner);
            case FLOOR_CHANGE:
                break; //TODO: Implement
            case SHOOT:
                return new Shoot(owner);
            case PUNCH:
                return new Punch(owner);
            case LOOT:
                break; //TODO: Implement
            case MARSHAL:
                break; //TODO: Implement
            default:
                return null;
        }

        return null;
    }
}
