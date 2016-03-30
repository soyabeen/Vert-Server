package ch.uzh.ifi.seal.soprafs16;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@EnableAutoConfiguration
public class Application {


    public Application() {
        /**
         * Spring needs this!!!
         */
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}