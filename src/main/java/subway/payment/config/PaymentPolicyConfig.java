package subway.payment.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.path.domain.PaymentPolicy;
import subway.payment.domain.payment.PaymentPolicyComposite;

@Configuration
public class PaymentPolicyConfig {

    @Bean
    public PaymentPolicy paymentPolicy(List<PaymentPolicy> paymentPolicies) {
        return new PaymentPolicyComposite(paymentPolicies);
    }
}
