package subway.domain.fare;

import subway.domain.section.Distance;

public interface FareStrategy {

    Fare calculate(final Distance distance);

    boolean isSatisfied(final Distance distance);
}
