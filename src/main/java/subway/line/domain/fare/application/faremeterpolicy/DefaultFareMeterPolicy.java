package subway.line.domain.fare.application.faremeterpolicy;

import org.springframework.stereotype.Component;
import subway.line.domain.fare.Fare;
import subway.line.domain.section.domain.Distance;

@Component
public class DefaultFareMeterPolicy implements FareMeterPolicy {
    private static final Distance MAX_DISTANCE = Distance.of(9);

    @Override
    public boolean support(CustomerCondition customerCondition) {
        return customerCondition.getDistance().isLessThanOrEquals(MAX_DISTANCE);
    }

    @Override
    public Fare calculateFare(CustomerCondition customerCondition) {
        return new Fare();
    }
}
