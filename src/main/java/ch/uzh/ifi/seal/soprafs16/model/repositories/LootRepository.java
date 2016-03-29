package ch.uzh.ifi.seal.soprafs16.model.repositories;

import ch.uzh.ifi.seal.soprafs16.model.Loot;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by alexanderhofmann on 26/03/16.
 */
@Repository("lootRepository")
public interface LootRepository extends CrudRepository<Loot, Long> {
}
