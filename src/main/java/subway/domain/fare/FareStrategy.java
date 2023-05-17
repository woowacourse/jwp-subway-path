package subway.domain.fare;

import subway.domain.subway.Passenger;
import subway.domain.subway.Subway;

public interface FareStrategy {

    long calculateFare(final long fare, final Passenger passenger, final Subway subway);
}
