package subway.domain.fare;

import org.springframework.stereotype.Component;

@Component
public final class AgeFareStrategyImpl implements AgeFareStrategy {

    private static final int TEENAGER_AGE = 13;
    private static final int ADULT_AGE = 19;
    private static final int DEDUCTION_FARE = 350;

    @Override
    public int calculate(final int age, final int fare) {
        if (age > ADULT_AGE) {
            return fare;
        }
        if (age > TEENAGER_AGE) {
            return calculateTeenager(fare);
        }

        return calculateChildren(fare);
    }

    private int calculateChildren(final int fare) {
        return (fare - DEDUCTION_FARE) / 2;
    }

    private int calculateTeenager(final int fare) {
        return (fare - DEDUCTION_FARE) * 4 / 5;
    }
}
