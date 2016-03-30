package ch.uzh.ifi.seal.soprafs16.model.repositories;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Round;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by soyabeen on 24.03.16.
 */
@Repository("roundRepository")
public interface RoundRepository extends CrudRepository<Round, Long> {
    Round findByGameAndNthRound(Game game, Integer nthRound);
}
