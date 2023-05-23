package subway.domain.fare.strategy;

import subway.domain.fare.Fare;
import subway.domain.section.Distance;

public interface FareStrategy {

    boolean isApplicable(Distance distance);

    Fare calculate(Distance distance);
}
