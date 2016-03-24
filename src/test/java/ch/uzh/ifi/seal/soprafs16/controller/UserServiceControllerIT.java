package ch.uzh.ifi.seal.soprafs16.controller;

import java.net.URL;
import java.util.List;

//import static org.hamcrest.Matchers.is;
//import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import ch.uzh.ifi.seal.soprafs16.Application;
import ch.uzh.ifi.seal.soprafs16.model.User;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({ "server.port=0" })
public class UserServiceControllerIT {

    @Value("${local.server.port}")
    private int          port;

    private URL          base;
    private RestTemplate template;

    @Before
    public void setUp()
            throws Exception {
        this.base = new URL("http://localhost:" + port + "/");
        this.template = new TestRestTemplate();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCreateUserSuccess() {
        List<User> usersBefore = template.getForObject(base + "/user", List.class);
        Assert.assertEquals(0, usersBefore.size());

        User request = new User("Mike Meyers", "mm");

        HttpEntity<User> httpEntity = new HttpEntity<User>(request);

        ResponseEntity<User> response = template.exchange(base + "/user/", HttpMethod.POST, httpEntity, User.class);
        Assert.assertSame(1L, response.getBody().getId());

        List<User> usersAfter = template.getForObject(base + "/user", List.class);
        Assert.assertEquals(1, usersAfter.size());

        ResponseEntity<User> userResponseEntity = template.getForEntity(base + "/user/" + response.getBody().getId(), User.class);
        User userResponse = userResponseEntity.getBody();
        Assert.assertEquals(request.getName(), userResponse.getName());
        Assert.assertEquals(request.getUsername(), userResponse.getUsername());
    }

}
