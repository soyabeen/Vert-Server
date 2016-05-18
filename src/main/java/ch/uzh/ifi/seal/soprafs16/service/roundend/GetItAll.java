package ch.uzh.ifi.seal.soprafs16.service.roundend;

import ch.uzh.ifi.seal.soprafs16.constant.LootType;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Loot;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devuser on 15.05.2016.
 */
public class GetItAll implements RoundEnd {

    @Override
    public List<Positionable> execute(Game game) {
        // put strongbox in waggon where the marshal is
        // !! always the same additional strongbox, do not introduce a new one !!
        List<Positionable> result = new ArrayList<>();

        Loot marshalsStrongbox = new Loot(LootType.STRONGBOX, game.getId(), 1000, game.getPositionMarshal(), Positionable.Level.BOTTOM);
        result.add(marshalsStrongbox);

        return result;
    }
}
