package subway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import subway.domain.fee.FeeStrategy;
import subway.domain.fee.NormalFeeStrategy;
import subway.domain.path.PathFindByDistanceStrategy;
import subway.domain.path.PathFindStrategy;

@Configuration
public class SubwayConfiguration {
    @Bean
    public FeeStrategy feeStrategy() {
        return new NormalFeeStrategy();
    }

    @Bean
    public PathFindStrategy pathFindStrategy() {
        return new PathFindByDistanceStrategy();
    }
}
