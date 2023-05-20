package subway.domain.subway.discount_policy;

import java.util.Arrays;
import org.springframework.stereotype.Component;
import subway.domain.passenger.Age;
import subway.domain.passenger.Passenger;
import subway.domain.subway.billing_policy.Fare;
import subway.exception.InvalidAgeException;

@Component
public final class DiscountPolicyByAge implements DiscountPolicy {

    private static final Priority PRIORITY = Priority.AGE_POLICY;

    enum AgeGroup {

        INFANT(new Age(0), new Age(5), 1),
        CHILD(new Age(6), new Age(12), 0.5),
        TEENAGER(new Age(13), new Age(18), 0.2),
        ADULT(new Age(19), new Age(150), 0);

        private final Age lowerBound;
        private final Age upperBound;
        private final double discountRate;

        AgeGroup(final Age lowerBound, final Age upperBound, final double discountRate) {
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
            this.discountRate = discountRate;
        }

        private static double findFareRate(Age age) {
            return Arrays.stream(values())
                    .filter(ageGroup -> age.isOlderThanOrEqualTo(ageGroup.lowerBound)
                            && age.isYoungerThanOrEqualTo(ageGroup.upperBound))
                    .findAny()
                    .orElseThrow(() -> new InvalidAgeException("나이가 포함되는 그룹이 없습니다."))
                    .discountRate;
        }
    }

    @Override
    public Fare calculateDiscountedFare(final Fare fare, final Passenger passenger) {
        final double discountRate = AgeGroup.findFareRate(passenger.getAge());
        if (isOneHundredPercent(discountRate)) {
            return new Fare(0);
        }
        return fare.subtract(fare.subtract(350).multiply(discountRate));
    }

    private boolean isOneHundredPercent(final double discountRate) {
        return discountRate == 1;
    }

    @Override
    public Priority getPriority() {
        return PRIORITY;
    }
}
