package subway.domain.subway;

import subway.domain.common.Distance;
import subway.exception.InvalidSectionDistanceException;

public class Section {

	private final Station upStation;
	private final Station downStation;
	private final Distance distance;

	public Section(final Station upStation, final Station downStation, final long distance) {
		this.upStation = upStation;
		this.downStation = downStation;
		this.distance = new Distance(distance);
	}

	public boolean hasStation(final Station station) {
		return this.upStation.equals(station) || this.downStation.equals(station);
	}

	public void validateSectionDistance(final long requestDistance) {
		if (distance.isSameOrUnder(requestDistance)) {
			throw new InvalidSectionDistanceException();
		}
	}

	public Station getUpStation() {
		return upStation;
	}

	public Station getDownStation() {
		return downStation;
	}

	public long getDistance() {
		return distance.getDistance();
	}
}
