package subway.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.domain.fee.DistanceFeePolicy;
import subway.domain.fee.FeePolicies;
import subway.domain.fee.FeePolicy;
import subway.domain.fee.LineFeePolicy;

@Configuration
public class FeeConfiguration {

    @Bean
    public FeePolicy feePolicy() {
        return new FeePolicies(List.of(new LineFeePolicy(), new DistanceFeePolicy()));
    }
}
