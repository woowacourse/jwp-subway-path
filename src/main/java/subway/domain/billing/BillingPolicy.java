package subway.domain.billing;

public interface BillingPolicy {

    int calculateFee(final int distance);
}
