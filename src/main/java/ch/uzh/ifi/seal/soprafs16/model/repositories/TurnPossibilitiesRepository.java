package ch.uzh.ifi.seal.soprafs16.model.repositories;

import ch.uzh.ifi.seal.soprafs16.model.TurnPossibilities;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by mirkorichter on 19.04.16.
 */

@Repository("turnRepository")
public interface TurnPossibilitiesRepository extends CrudRepository<TurnPossibilities, Long> {
    TurnPossibilities findById(Long id);
}
