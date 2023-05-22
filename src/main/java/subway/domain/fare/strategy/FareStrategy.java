package subway.domain.fare.strategy;

import subway.domain.Distance;
import subway.domain.fare.Fare;

public interface FareStrategy {

    boolean isApplicable(Distance distance);

    Fare calculate(Distance distance);
}
