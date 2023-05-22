package subway.line.domain.fare.application.faremeterpolicy;

import subway.line.domain.fare.Fare;

public interface FareMeterPolicy {
    boolean support(CustomerCondition customerCondition);

    Fare calculateFare(CustomerCondition customerCondition);
}
