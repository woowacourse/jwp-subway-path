package subway.domain.payment;

import subway.domain.Lines;

public class PaymentLines {

    private final Lines lines;
    private final PaymentPolicy paymentPolicy;

    public PaymentLines(final Lines lines, final PaymentPolicy paymentPolicy) {
        this.lines = lines;
        this.paymentPolicy = paymentPolicy;
    }

    public int calculateFee() {
        return paymentPolicy.calculateFee(lines);
    }

    public Lines lines() {
        return lines;
    }
}
