package engine;

import engine.cards.Card;
import engine.rules.RuleEngineFactory;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by alex on 19.03.2016.
 */
public class Round {
    public static final Logger log = Logger.getLogger(Round.class.getName());

    private List<Card> playedCards;

    public Round(List<Card> playedCards) {
        this.playedCards = playedCards;
    }

    public void executeRound(){
        log.info("Executing round!");
        log.info("Cards played: " + playedCards.size());

        for (Card currentCard : playedCards) {
            log.info(currentCard.getOwner().getName() + " played: " + currentCard.getType());
            RuleEngineFactory cardRule = RuleEngineFactory.create(currentCard);
            cardRule.execute();
        }
    }
}
