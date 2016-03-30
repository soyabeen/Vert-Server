package ch.uzh.ifi.seal.soprafs16.utility;

import ch.uzh.ifi.seal.soprafs16.constant.LootType;
import ch.uzh.ifi.seal.soprafs16.model.Loot;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import ch.uzh.ifi.seal.soprafs16.model.repositories.LootRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Created by alexanderhofmann on 29/03/16.
 */
@Service
public class LootBuilder {

    @Autowired
    private LootRepository lootRepo;

    private LootType[] types = LootType.values();


    /**
     * Generates a random piece of loot.
     * Type, value and position are all generated randomly.
     * @return random loot
     */
    public Loot getRandomLoot() {
        Random r = new Random();
        LootType randomType = types[r.nextInt(types.length)];
        int value = r.nextInt(750) + 250;
        Positionable.Level level = r.nextBoolean() ? Positionable.Level.BOTTOM : Positionable.Level.TOP;
        Loot loot = new Loot(randomType, value, level);
        return loot;
    }

    /**
     * Generates a random piece of loot and saves it.
     * {@see getRandomLoot()}
     * @return random saved loot
     */
    public Loot getRandomLootAndSave() {
        return lootRepo.save(getRandomLoot());
    }
}
