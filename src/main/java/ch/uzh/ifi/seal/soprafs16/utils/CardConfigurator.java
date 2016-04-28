package ch.uzh.ifi.seal.soprafs16.utils;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.model.Card;
import ch.uzh.ifi.seal.soprafs16.model.CardDeck;
import ch.uzh.ifi.seal.soprafs16.model.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by alexanderhofmann on 26/04/16.
 */
public class CardConfigurator {
    private static final int NR_OF_MOVE_CARDS = 2;
    private static final int NR_OF_FLOORCHANGE_CARDS = 2;
    private static final int NR_OF_BULLET_CARDS = 0;
    private static final int NR_OF_FIRE_CARDS = 2;
    private static final int NR_OF_PUNCH_CARDS = 1;
    private static final int NR_OF_ROBBERY_CARDS = 2;
    private static final int NR_OF_MARSHAL_CARDS = 1;

    private Long ownerId;

    public CardConfigurator(Long ownerId) {
        this.ownerId = ownerId;
    }

    public CardDeck buildDeck() {
        List<Card> cards = new ArrayList<>();

        cards.addAll(createCardsForType(ownerId, CardType.MOVE, NR_OF_MOVE_CARDS));
        cards.addAll(createCardsForType(ownerId, CardType.FLOORCHANGE, NR_OF_FLOORCHANGE_CARDS));
        cards.addAll(createCardsForType(ownerId, CardType.BULLET, NR_OF_BULLET_CARDS));
        cards.addAll(createCardsForType(ownerId, CardType.FIRE, NR_OF_FIRE_CARDS));
        cards.addAll(createCardsForType(ownerId, CardType.PUNCH, NR_OF_PUNCH_CARDS));
        cards.addAll(createCardsForType(ownerId, CardType.ROBBERY, NR_OF_ROBBERY_CARDS));
        cards.addAll(createCardsForType(ownerId, CardType.MARSHAL, NR_OF_MARSHAL_CARDS));

        Collections.shuffle(cards);
        CardDeck deck = new CardDeck(cards);

        return deck;
    }

    private List<Card> createCardsForType(Long ownerId, CardType type, final int nrOfCards) {
        List<Card> cards = new ArrayList<>();

        for (int i = 0; i < nrOfCards; i++) {
            cards.add(new Card(type, ownerId));
        }

        return cards;
    }
}
