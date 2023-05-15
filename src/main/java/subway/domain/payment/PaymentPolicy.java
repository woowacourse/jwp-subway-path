package subway.domain.payment;

import subway.domain.Path;

public interface PaymentPolicy {

    int calculateFee(final Path path);
}
