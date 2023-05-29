package subway.domain.fare.distance;

import subway.domain.line.Distance;

public class LongDistanceFareStrategy implements DistanceBasedFareStrategy {

	private final int minDistance;
	private final int unitDistance;
	private final int surcharge;

	public LongDistanceFareStrategy(final int minDistance, final int unitDistance, final int surcharge) {
		this.minDistance = minDistance;
		this.unitDistance = unitDistance;
		this.surcharge = surcharge;
	}

	@Override
	public int calculate(final Distance distance) {
		final int distanceValue = distance.getValue();

		if (distanceValue <= minDistance) {
			return 0;
		}
		final int effectiveDistance = distanceValue - minDistance;
		return calculateOverFare(effectiveDistance, unitDistance);
	}

	private int calculateOverFare(int distance, int unitDistance) {
		return (int)((Math.ceil((distance - 1) / unitDistance) + 1) * surcharge);
	}
}
