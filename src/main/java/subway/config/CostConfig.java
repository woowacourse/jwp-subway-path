package subway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.application.costPolicy.AgeCostPolicyChain;
import subway.application.costPolicy.CostPolicyChain;
import subway.application.costPolicy.DistanceCostPolicyChain;

@Configuration
public class CostConfig {

    @Bean
    public CostPolicyChain costPolicyChain() {
        final CostPolicyChain costPolicyChain = new DistanceCostPolicyChain();
        costPolicyChain.setNext(new AgeCostPolicyChain());
        return costPolicyChain;
    }
}
