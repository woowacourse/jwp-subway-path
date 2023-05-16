package subway.domain.billing;

public interface BillingPolicy {

    int calculateFare(final int distance);
}
