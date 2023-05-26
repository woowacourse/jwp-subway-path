package subway.domain.line;

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

	public boolean isTerminalConnected(final Section upLineTerminal, final Section downLineTerminal) {
		return isConnectedToUpLine(upLineTerminal) || isConnectedToDownLine(downLineTerminal);
	}

	public boolean isCrossConnected(final Section upLineTerminal, final Section downLineTerminal) {
		return isConnectedToDownLine(downLineTerminal) && isConnectedToUpLine(upLineTerminal);
	}

	public boolean isConnected(final Section section) {
		return isSameDeparture(section) || isSameArrival(section);
	}

	public boolean isSameDeparture(final Section newSection) {
		return this.departure.equals(newSection.departure);
	}

	public boolean containStation(final Station station) {
		return departure.equals(station) || arrival.equals(station);
	}

	public Distance addDistance(final Section newSection) {
		return this.distance.addDistance(newSection.distance);
	}

	public Distance subtractDistance(final Section newSection) {
		return this.distance.subtractDistance(newSection.distance);
	}

	private boolean isConnectedToUpLine(final Section section) {
		return this.arrival.equals(section.departure);
	}

	private boolean isConnectedToDownLine(final Section section) {
		return this.departure.equals(section.arrival);
	}

	private boolean isSameArrival(final Section section) {
		return this.arrival.equals(section.arrival);
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
