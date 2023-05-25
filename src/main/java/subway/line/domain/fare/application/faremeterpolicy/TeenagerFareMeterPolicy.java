package subway.line.domain.fare.application.faremeterpolicy;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import subway.line.domain.fare.Fare;
import subway.line.domain.fare.application.domain.Age;
import subway.line.domain.fare.dto.CustomerCondition;

import java.math.BigDecimal;

@Component
@Order(4)
public class TeenagerFareMeterPolicy implements FareMeterPolicy {
    public static final Age MIN_TEENAGER_AGE = Age.of(13);
    public static final Age MAX_TEENAGER_AGE = Age.of(19);
    public static final Fare DEDUCTION = new Fare(new BigDecimal("350"));
    public static final double DISCOUNT_PERCENTAGE = 0.2;

    @Override
    public boolean support(CustomerCondition customerCondition) {
        final var age = customerCondition.getAge();
        if (age == null) {
            return false;
        }
        return age.isMoreThanOrEquals(MIN_TEENAGER_AGE) && age.isLessThanOrEquals(MAX_TEENAGER_AGE);
    }

    @Override
    public Fare calculateFare(Fare fare, CustomerCondition customerCondition) {
        final var deductedFare = fare.subtract(DEDUCTION);
        return deductedFare.discountPercentage(DISCOUNT_PERCENTAGE);
    }
}
