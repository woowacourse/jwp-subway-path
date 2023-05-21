package subway.domain.fare.policy;

import subway.domain.fare.FareInformation;

public interface FarePolicy {

    int calculate(final FareInformation fareInformation);
}
