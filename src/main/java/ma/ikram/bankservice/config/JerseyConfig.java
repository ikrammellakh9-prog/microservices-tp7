package ma.ikram.bankservice.config;

import ma.ikram.bankservice.rest.CompteRestJerseyResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(CompteRestJerseyResource.class);
    }
}
