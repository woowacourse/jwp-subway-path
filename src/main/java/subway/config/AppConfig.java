package subway.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.domain.fare.BasicDistanceFareStrategy;
import subway.domain.fare.ExtraDistanceFareStrategy;
import subway.domain.fare.FareCalculator;

@Configuration
public class AppConfig {
    
    @Bean
    FareCalculator fareCalculator() {
        return new FareCalculator(
                List.of(new BasicDistanceFareStrategy(), new ExtraDistanceFareStrategy())
        );
    }
    
}
