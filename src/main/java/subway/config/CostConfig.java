package subway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.application.costpolicy.AgeCostPolicyChain;
import subway.application.costpolicy.CostPolicyChain;
import subway.application.costpolicy.DistanceCostPolicyChain;
import subway.application.costpolicy.LineCostPolicyChain;

@Configuration
public class CostConfig {

    @Bean
    public CostPolicyChain costPolicyChain() {
        final CostPolicyChain costPolicyChain = new DistanceCostPolicyChain();
        final CostPolicyChain ageCostPolicyChain = new AgeCostPolicyChain();
        final CostPolicyChain lineCostPolicyChain = new LineCostPolicyChain();
        costPolicyChain.setNext(ageCostPolicyChain);
        ageCostPolicyChain.setNext(lineCostPolicyChain);
        return costPolicyChain;
    }
}
