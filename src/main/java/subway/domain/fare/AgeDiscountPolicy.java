package subway.domain.fare;

import subway.domain.route.Route;

public class AgeDiscountPolicy implements FarePolicy {

    @Override
    public Fare calculate(final Route route, final Integer age, Fare fare) {
        AgeFareRate ageFareRate = AgeFareRate.from(age);
        fare = fare.minus(ageFareRate.getDiscountFare());
        fare = fare.discountByRate(ageFareRate.getDiscountRate());
        return fare;
    }
}
