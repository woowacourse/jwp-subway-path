package subway.line.domain.fare.application;

import org.springframework.stereotype.Component;
import subway.line.domain.fare.Fare;
import subway.line.domain.fare.application.exception.CalculatingFareNotPossibleException;
import subway.line.domain.fare.application.faremeterpolicy.CustomerCondition;
import subway.line.domain.fare.application.faremeterpolicy.FareMeterPolicy;

import java.util.List;

@Component
public class SubwayFareMeter {
    private final List<FareMeterPolicy> fareMeterPolicies;

    public SubwayFareMeter(List<FareMeterPolicy> fareMeterPolicies) {
        this.fareMeterPolicies = fareMeterPolicies;
    }

    public Fare calculateFare(CustomerCondition customerCondition) {
        for (FareMeterPolicy fareMeterPolicy : fareMeterPolicies) {
            if (fareMeterPolicy.support(customerCondition)) {
                return fareMeterPolicy.calculateFare(customerCondition);
            }
        }
        throw new CalculatingFareNotPossibleException();
    }
}
