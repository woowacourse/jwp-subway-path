package subway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.domain.fee.FeeStrategy;
import subway.domain.fee.NormalFeeStrategy;
import subway.domain.path.PathStrategy;
import subway.domain.path.ShortestDistancePathStrategy;

@Configuration
public class Config {
    @Bean
    public FeeStrategy feeStrategy() {
        return new NormalFeeStrategy();
    }

    @Bean
    public PathStrategy pathFindStrategy() {
        return new ShortestDistancePathStrategy();
    }
}
