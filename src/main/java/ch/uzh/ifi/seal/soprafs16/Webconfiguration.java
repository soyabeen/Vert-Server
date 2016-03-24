package ch.uzh.ifi.seal.soprafs16;

import org.h2.server.web.WebServlet;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by antoio on 3/24/16.
 */
@Configuration
public class Webconfiguration {

    @Bean
    ServletRegistrationBean h2servletRegistration() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean( new WebServlet() );
        registrationBean.addUrlMappings("/console/*");
        return registrationBean;
    }
}
