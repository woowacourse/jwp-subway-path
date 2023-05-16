package subway.domain.subway.billing_policy;

import subway.domain.Path;

public interface BillingPolicy {

    Fare calculateFare(Path path);
}
