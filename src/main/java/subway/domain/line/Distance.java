package subway.domain.line;

import java.util.Objects;

import subway.error.exception.SectionDistanceException;

public final class Distance {

	private final int distance;

	public Distance(final int distance) {
		validatePositive(distance);
		this.distance = distance;
	}

	public Distance addDistance(final Distance distance) {
		final int result = this.distance + distance.distance;

		return new Distance(result);
	}

	public Distance subtractDistance(final Distance distance) {
		final int result = this.distance - distance.distance;

		return new Distance(result);
	}

	private static void validatePositive(int distance) {
		if (distance <= 0) {
			throw SectionDistanceException.EXCEPTION;
		}
	}

	public int getValue() {
		return distance;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final Distance distance1 = (Distance)o;
		return distance == distance1.distance;
	}

	@Override
	public int hashCode() {
		return Objects.hash(distance);
	}
}
