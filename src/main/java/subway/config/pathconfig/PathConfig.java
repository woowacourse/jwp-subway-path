package subway.config.pathconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.domain.path.PathFinder;

@Configuration
public class PathConfig {

    @Bean
    public PathFinder pathFinder() {
        return new PathFinder();
    }
}
