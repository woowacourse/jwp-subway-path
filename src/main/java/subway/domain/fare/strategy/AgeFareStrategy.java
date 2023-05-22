package subway.domain.fare.strategy;

import org.springframework.stereotype.Component;
import subway.domain.fare.FareInfo;

@Component
public final class AgeFareStrategy implements FareStrategy {

    private static final int TEENAGER_AGE = 13;
    private static final int ADULT_AGE = 19;
    private static final int DEDUCTION_FARE = 350;

    @Override
    public FareInfo calculate(final FareInfo fareInfo) {
        if (fareInfo.isOlderThan(ADULT_AGE)) {
            return fareInfo;
        }
        if (fareInfo.isOlderThan(TEENAGER_AGE)) {
            return calculateTeenager(fareInfo);
        }

        return calculateChildren(fareInfo);
    }

    private FareInfo calculateChildren(final FareInfo fareInfo) {
        final int fare = (fareInfo.getFare() - DEDUCTION_FARE) / 2;

        return fareInfo.updateFare(fare);
    }

    private FareInfo calculateTeenager(final FareInfo fareInfo) {
        final int fare = (fareInfo.getFare() - DEDUCTION_FARE) * 4 / 5;

        return fareInfo.updateFare(fare);
    }
}
