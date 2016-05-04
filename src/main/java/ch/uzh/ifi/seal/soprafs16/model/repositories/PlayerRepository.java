package ch.uzh.ifi.seal.soprafs16.model.repositories;

import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by soyabeen on 24.03.16.
 */
@Repository("playerRepository")
public interface PlayerRepository extends CrudRepository<Player, Long> {
    Player findByUsername(String username);
    Player findByToken(String token);
}
