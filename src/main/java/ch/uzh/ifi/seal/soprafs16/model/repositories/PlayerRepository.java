package ch.uzh.ifi.seal.soprafs16.model.repositories;

import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by soyabeen on 24.03.16.
 */
@Repository("playerRepository")
public interface PlayerRepository extends CrudRepository<Player, Long> {

}
