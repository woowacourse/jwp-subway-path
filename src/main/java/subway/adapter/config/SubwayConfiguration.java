package subway.adapter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.adapter.out.graph.ShortPathAdapter;
import subway.application.port.out.graph.ShortPathPort;

@Configuration
public class SubwayConfiguration {

    @Bean
    public ShortPathPort shortPathPort() {
        return new ShortPathAdapter();
    }
}
