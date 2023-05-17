package subway.path.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.path.domain.payment.PaymentPolicy;
import subway.path.domain.payment.PaymentPolicyComposite;

@Configuration
public class PaymentPolicyConfig {

    @Bean
    public PaymentPolicy paymentPolicy(List<PaymentPolicy> paymentPolicies) {
        return new PaymentPolicyComposite(paymentPolicies);
    }
}
