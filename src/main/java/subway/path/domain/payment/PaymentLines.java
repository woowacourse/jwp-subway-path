package subway.path.domain.payment;

import subway.path.domain.Path;

public class PaymentLines {

    private final Path path;
    private final PaymentPolicy paymentPolicy;

    public PaymentLines(final Path path, final PaymentPolicy paymentPolicy) {
        this.path = path;
        this.paymentPolicy = paymentPolicy;
    }

    public int calculateFee() {
        return paymentPolicy.calculateFee(path);
    }

    public Path lines() {
        return path;
    }
}
