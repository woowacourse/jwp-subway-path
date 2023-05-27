package subway.domain.fare.distance;

import java.util.List;

import subway.domain.fare.FarePolicy;
import subway.domain.line.Distance;

public class DistanceBasedFarePolicy implements FarePolicy {

	private static final List<DistanceBasedFareStrategy> distanceBasedFareStrategy = List.of(
		new MidDistanceFareStrategy(10, 50, 5, 100),
		new LongDistanceFareStrategy(50, 8, 100)
	);

	private final Distance distance;

	public DistanceBasedFarePolicy(final Distance distance) {
		this.distance = distance;
	}

	@Override
	public int calculate(final int defaultFare) {
		final int extraFare = distanceBasedFareStrategy.stream()
			.mapToInt(farePolicy -> farePolicy.calculate(distance))
			.sum();

		return defaultFare + extraFare;
	}

}
