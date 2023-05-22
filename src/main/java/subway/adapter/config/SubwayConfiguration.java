package subway.adapter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.adapter.out.graph.ShortPathAdapter;
import subway.application.port.out.graph.ShortPathHandler;

@Configuration
public class SubwayConfiguration {

    @Bean
    public ShortPathHandler shortPathPort() {
        return new ShortPathAdapter();
    }
}
