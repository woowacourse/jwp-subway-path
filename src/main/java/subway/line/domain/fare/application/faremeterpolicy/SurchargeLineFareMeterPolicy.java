package subway.line.domain.fare.application.faremeterpolicy;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import subway.line.domain.fare.Fare;
import subway.line.domain.fare.dto.CustomerCondition;

import java.util.Collections;

@Component
@Order(3)
public class SurchargeLineFareMeterPolicy implements FareMeterPolicy {
    @Override
    public boolean support(CustomerCondition customerCondition) {
        return !customerCondition.getSurcharges().isEmpty();
    }

    @Override
    public Fare calculateFare(Fare fare, CustomerCondition customerCondition) {
        final var surcharges = customerCondition.getSurcharges();
        final var maxSurcharge = Collections.max(surcharges);
        return fare.add(maxSurcharge);
    }
}
