package subway.path.domain.payment;

import subway.path.domain.Path;

public interface PaymentPolicy {

    int calculateFee(final Path path);
}
