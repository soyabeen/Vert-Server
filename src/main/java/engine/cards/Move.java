package engine.cards;

import engine.User;

/**
 * Created by alex on 17.03.2016.
 */
public class Move extends Card {

    public Move(User owner){
        super(CardType.MOVE, owner);
    }
}
