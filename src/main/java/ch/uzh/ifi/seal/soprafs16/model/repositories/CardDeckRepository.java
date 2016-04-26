package ch.uzh.ifi.seal.soprafs16.model.repositories;

import ch.uzh.ifi.seal.soprafs16.model.CardDeck;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by alexanderhofmann on 26/04/16.
 */
public interface CardDeckRepository extends CrudRepository<CardDeck, Long> {
}
