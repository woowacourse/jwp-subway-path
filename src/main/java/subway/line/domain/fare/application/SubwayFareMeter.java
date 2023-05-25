package subway.line.domain.fare.application;

import org.springframework.stereotype.Component;
import subway.line.domain.fare.Fare;
import subway.line.domain.fare.application.exception.CalculatingFareNotPossibleException;
import subway.line.domain.fare.dto.CustomerCondition;
import subway.line.domain.fare.application.faremeterpolicy.FareMeterPolicy;

import java.util.ArrayDeque;
import java.util.List;

@Component
public class SubwayFareMeter {
    private final List<FareMeterPolicy> fareMeterPolicies;

    public SubwayFareMeter(List<FareMeterPolicy> fareMeterPolicies) {
        this.fareMeterPolicies = fareMeterPolicies;
    }

    public Fare calculateFare(CustomerCondition customerCondition) {
        final var policyDeque = new ArrayDeque<>(fareMeterPolicies);
        Fare fare = new Fare();

        while (!policyDeque.isEmpty()) {
            final var policy = policyDeque.poll();
            if (policy.support(customerCondition)) {
                fare = policy.calculateFare(fare, customerCondition);
            }
        }

        if (fare == null) {
            throw new CalculatingFareNotPossibleException();
        }
        return fare;
    }
}
