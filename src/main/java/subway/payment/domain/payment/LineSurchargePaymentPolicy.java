package subway.payment.domain.payment;

import org.springframework.stereotype.Component;
import subway.line.domain.Line;
import subway.path.domain.Path;
import subway.path.domain.PaymentPolicy;

@Component
public class LineSurchargePaymentPolicy implements PaymentPolicy {

    @Override
    public int calculateFee(final Path path) {
        return path.lines()
                .stream()
                .mapToInt(Line::surcharge)
                .max()
                .orElse(0);
    }
}
