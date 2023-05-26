package subway.domain.fee;

import subway.domain.line.Distance;

public class Fee {

	private static final int DEFAULT_FEE = 1250;
	private static final int FIRST_DISTANCE_LIMIT = 10;
	private static final int SECOND_DISTANCE_LIMIT = 50;
	private static final int FIRST_UNIT_DISTANCE = 5;
	private static final int SECOND_UNIT_DISTANCE = 8;
	private static final int SURCHARGE = 100;

	private final int fee;

	private Fee(final int fee) {
		this.fee = fee;
	}

	public static Fee Calculate(final Distance distance) {
		final int distanceValue = distance.getValue();

		if (distanceValue < FIRST_DISTANCE_LIMIT) {
			return new Fee(DEFAULT_FEE);
		}

		if (distanceValue <= SECOND_DISTANCE_LIMIT) {
			final int fee = DEFAULT_FEE
				+ calculateOverFare(distanceValue - FIRST_DISTANCE_LIMIT, FIRST_UNIT_DISTANCE);
			return new Fee(fee);
		}

		final int fee = DEFAULT_FEE
			+ calculateOverFare(SECOND_DISTANCE_LIMIT - FIRST_DISTANCE_LIMIT, FIRST_UNIT_DISTANCE)
			+ calculateOverFare(distanceValue - SECOND_DISTANCE_LIMIT, SECOND_UNIT_DISTANCE);
		return new Fee(fee);

	}

	private static int calculateOverFare(int distance, int unitDistance) {
		return (int)((Math.ceil((distance - 1) / unitDistance) + 1) * SURCHARGE);
	}

	public int getFee() {
		return fee;
	}
}
