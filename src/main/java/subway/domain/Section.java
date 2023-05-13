package subway.domain;

import java.util.Objects;

public final class Section {

	private final Long id;
	private final Station departure;
	private final Station arrival;
	private final Distance distance;

	public Section(final Long id, final Station departure, final Station arrival, final Distance distance) {
		this.id = id;
		this.departure = departure;
		this.arrival = arrival;
		this.distance = distance;
	}

	public Long getId() {
		return id;
	}

	public Station getDeparture() {
		return departure;
	}

	public Station getArrival() {
		return arrival;
	}

	public Distance getDistance() {
		return distance;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final Section section = (Section)o;
		return Objects.equals(id, section.id) && Objects.equals(departure, section.departure)
			&& Objects.equals(arrival, section.arrival) && Objects.equals(distance, section.distance);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, departure, arrival, distance);
	}
}
