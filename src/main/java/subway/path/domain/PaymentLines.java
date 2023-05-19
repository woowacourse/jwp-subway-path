package subway.path.domain;

import subway.payment.domain.discount.DiscountResult;

public class PaymentLines {

    private final Path path;
    private final PaymentPolicy paymentPolicy;
    private final DiscountPolicy discountPolicy;

    public PaymentLines(final Path path,
                        final PaymentPolicy paymentPolicy,
                        final DiscountPolicy discountPolicy) {
        this.path = path;
        this.paymentPolicy = paymentPolicy;
        this.discountPolicy = discountPolicy;
    }

    public DiscountResult discountFee() {
        return discountPolicy.discount(calculateFee());
    }

    private int calculateFee() {
        return paymentPolicy.calculateFee(path);
    }

    public Path lines() {
        return path;
    }
}
