package ch.uzh.ifi.seal.soprafs16.dto.mapper;

import ch.uzh.ifi.seal.soprafs16.dto.GameWithCurrentCardDTO;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Created by soyabeen on 02.05.16.
 */
@Mapper
public interface GameWithLastPlayedCardMapper {

    GameWithLastPlayedCardMapper INSTANCE = Mappers.getMapper(GameWithLastPlayedCardMapper.class);

    @Mapping(source = "id", target = "id")
    GameWithCurrentCardDTO toDTO(Game game);
}
