package subway.path.domain.payment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import subway.path.domain.Path;

public class PaymentPolicyComposite implements PaymentPolicy {

    private final List<PaymentPolicy> paymentPolicies;

    public PaymentPolicyComposite(final PaymentPolicy... paymentPolicies) {
        this(Arrays.asList(paymentPolicies));
    }

    public PaymentPolicyComposite(final List<PaymentPolicy> paymentPolicies) {
        this.paymentPolicies = new ArrayList<>(paymentPolicies);
    }

    @Override
    public int calculateFee(final Path path) {
        return paymentPolicies.stream()
                .mapToInt(it -> it.calculateFee(path))
                .sum();
    }
}
