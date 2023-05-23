package subway.line.domain.fare.application.faremeterpolicy;

import subway.line.domain.fare.Fare;
import subway.line.domain.fare.dto.CustomerCondition;

public interface FareMeterPolicy {
    boolean support(CustomerCondition customerCondition);

    Fare calculateFare(Fare fare, CustomerCondition customerCondition);
}
