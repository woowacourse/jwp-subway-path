package subway.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import subway.path.domain.payment.DefaultPaymentPolicy;
import subway.path.domain.payment.LineSurchargePaymentPolicy;
import subway.path.domain.payment.PaymentPolicy;
import subway.path.domain.payment.PaymentPolicyComposite;

@Configuration
public class PaymentPolicyConfig {

    @Bean
    public PaymentPolicy paymentPolicy() {
        return new PaymentPolicyComposite(
                new DefaultPaymentPolicy(),
                new LineSurchargePaymentPolicy()
        );
    }
}
