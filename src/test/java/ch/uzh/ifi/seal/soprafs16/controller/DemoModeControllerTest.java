package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URL;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({ "server.port=0" })
public class DemoModeControllerTest {

    private static final Logger logger  = LoggerFactory.getLogger(DemoModeControllerTest.class);

    @Value("${local.server.port}")
    private int          port;

    private URL          base;
    private RestTemplate template;


    @Before
    public void setUp() throws MalformedURLException {
        this.base = new URL("http://localhost:" + port + "/demos");
        this.template = new TestRestTemplate();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void addGameTest() {

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(base.toString());
        template.postForLocation(uriBuilder.toUriString(), null);
    }
}
