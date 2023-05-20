package subway.payment.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import subway.path.domain.PaymentPolicy;
import subway.payment.domain.payment.PaymentPolicyComposite;

@Configuration
public class PaymentPolicyConfig {

    @Primary
    @Bean
    public PaymentPolicy paymentPolicy(final List<PaymentPolicy> paymentPolicies) {
        return new PaymentPolicyComposite(paymentPolicies);
    }
}
