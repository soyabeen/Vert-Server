package ch.uzh.ifi.seal.soprafs16.model;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.service.ActionPhaseService;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;

/**
 * Created by devuser on 20.05.2016.
 */
public class CardTest {

    private static final Logger logger = LoggerFactory.getLogger(ActionPhaseService.class);

    @Test
    public void testOverridenEquals() {
        Card card1 = new Card();
        card1.setType(CardType.FIRE);
        card1.setId(1L);
        card1.setOwnerId(1L);
        Card card2 = new Card(CardType.FIRE, 2L);
        card2.setOwnerId(2L);

        Assert.assertTrue(!card1.equals(card2));
        Assert.assertTrue(card1.equals(card1));
    }

    @Test
    public void testCardHashCode() {
        Card card3 = new Card();
        card3.setType(CardType.FIRE);

        logger.debug("card3 has no 'ownerId' set");
        Assert.assertThat("Card has no 'ownerId' set", card3.hashCode(), not(0));

        Card card4 = new Card();
        card4.setOwnerId(4L);
        logger.debug("card4 has no card type");
        Assert.assertThat("Card has no type set", card4.hashCode(), not(0));

        Card card5 = new Card();
        card5.setType(CardType.FIRE);
        card5.setOwnerId(6L);
        logger.debug("card5 has card type and 'ownerId' set");
        Assert.assertThat("Card has 'ownerId' and type set", card5.hashCode(), not(0));

        Card card6 = new Card();
        logger.debug("card6 has nothing set");
        Assert.assertThat("Card has no 'ownerId' and no type set", card6.hashCode(), is(0));
    }


}
