package ch.uzh.ifi.seal.soprafs16.model.repositories;

import ch.uzh.ifi.seal.soprafs16.model.Turn;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by mirkorichter on 19.04.16.
 */

@Repository("turnRepository")
public interface TurnRepository extends CrudRepository<Turn, Long> {
    Turn findById(Long id);
}
