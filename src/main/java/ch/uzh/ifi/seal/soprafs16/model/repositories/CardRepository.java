package ch.uzh.ifi.seal.soprafs16.model.repositories;

import ch.uzh.ifi.seal.soprafs16.model.Card;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by alexanderhofmann on 27/04/16.
 */
public interface CardRepository extends CrudRepository<Card, Long> {
    Card findByOnHand(boolean onHand);
}
