package ch.uzh.seal.soprafs16.client;

import ch.uzh.seal.soprafs16.client.model.User;
import junit.framework.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
import org.springframework.web.client.RestTemplate;

/**
 * Test sequence for the game initialisation part.
 * <ol>
 * <li>get user</li>
 * <li>create new game</li>
 * <li>select character and create player</li>
 * </ol>
 * <strong>Important:</strong> Needs a running sopra server. URL is set in <code>baseUri</code> attribute
 * (Default connects to the locally deployed sopra server).
 * <br/>
 * Created by soyabeen on 31.03.16.
 */
public class GameInitialisationTest {

    private static final Logger logger = LoggerFactory.getLogger(GameInitialisationTest.class);

    //    private static final String baseUri = "https://sopra-fs16-group5.herokuapp.com";
    private static final String baseUri = "http://localhost:8080";


    @Test
    public void dofirstClientConnectionToGetNewUser() {
        User me = new User("Harry Potter", "TheWizard");
        logger.debug("Create new user for me: {}", me.toString());

        RestTemplate restTemplate = new RestTemplate();
        User registerdUser = restTemplate.postForObject(baseUri + "/users", me, User.class);
        logger.debug("Registered: {}", registerdUser.toString());
    }


}
