package subway.domain.fare.distance;

import subway.domain.line.Distance;

public interface DistanceBasedFareStrategy {

	int calculate(Distance distance);

}
