package subway.domain.fare.distance;

import subway.domain.line.Distance;

public class MidDistanceFareStrategy implements DistanceBasedFareStrategy {

	private final int minDistance;
	private final int maxDistance;
	private final int unitDistance;
	private final int surcharge;

	public MidDistanceFareStrategy(final int minDistance, final int maxDistance, final int unitDistance,
		final int surcharge) {
		this.minDistance = minDistance;
		this.maxDistance = maxDistance;
		this.unitDistance = unitDistance;
		this.surcharge = surcharge;
	}

	@Override
	public int calculate(final Distance distance) {
		final int distanceValue = distance.getValue();

		if (distanceValue < minDistance) {
			return 0;
		}

		int effectiveDistance = Math.min(distanceValue, maxDistance) - minDistance;
		return calculateOverFare(effectiveDistance, unitDistance);
	}

	private int calculateOverFare(int distance, int unitDistance) {
		return (int)((Math.ceil((distance - 1) / unitDistance) + 1) * surcharge);
	}
}
