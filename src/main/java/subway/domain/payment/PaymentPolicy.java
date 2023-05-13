package subway.domain.payment;

import subway.domain.Lines;

public interface PaymentPolicy {

    int calculateFee(final Lines lines);
}
