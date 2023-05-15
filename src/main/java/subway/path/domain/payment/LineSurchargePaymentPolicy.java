package subway.path.domain.payment;

import subway.line.domain.Line;
import subway.path.domain.Path;

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
