package engine;


import engine.cards.Card;
import engine.cards.CardType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class.getName());

    private Main(){

    }

    public static void main(String[] args) {


	    User player1 = new User("Player 1");
        User player2 = new User("Player 2");

        final String delimiter = "------";
        final String newRound = "New Round";

        List<Card> cardsPlayed = new ArrayList<>();


        //Empty deck between rounds
        log.info(delimiter);
        log.info(newRound);
        log.info(delimiter);

        //SCHEMING PHASE / PLAYING CARDS
        cardsPlayed.add(player1.playCard(CardType.MOVE));
        cardsPlayed.add(player2.playCard(CardType.MOVE));

        //ACTION PHASE
        Round round1 = new Round(cardsPlayed);
        round1.executeRound();
        log.info(player1.toString());
        log.info(player2.toString());

        //Empty deck between rounds
        cardsPlayed.clear();
        log.info(delimiter);
        log.info(newRound);
        log.info(delimiter);

        //SCHEMING PHASE / PLAYING CARDS
        cardsPlayed.add(player1.playCard(CardType.SHOOT));
        cardsPlayed.add(player1.playCard(CardType.MOVE));
        cardsPlayed.add(player1.playCard(CardType.SHOOT));

        //ACTION PHASE
        Round round2 = new Round(cardsPlayed);
        round2.executeRound();
        log.info(player1.toString());
        log.info(player2.toString());

        //Empty deck between rounds
        cardsPlayed.clear();
        log.info(delimiter);
        log.info(newRound);
        log.info(delimiter);

        //SCHEMING PHASE / PLAYING CARDS
        cardsPlayed.add(player1.playCard(CardType.PUNCH));
        cardsPlayed.add(player1.playCard(CardType.MOVE));
        cardsPlayed.add(player1.playCard(CardType.PUNCH));

        //ACTION PHASE
        Round round3 = new Round(cardsPlayed);
        round3.executeRound();
        log.info(player1.toString());
        log.info(player2.toString());
    }
}
