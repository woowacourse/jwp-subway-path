package subway.domain.fare;

import subway.domain.subway.Passenger;
import subway.domain.subway.Subway;

public interface FareStrategy {

    double calculateFare(final double fare, final Passenger passenger, final Subway subway);
}
