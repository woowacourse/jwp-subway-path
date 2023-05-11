package subway.domain;

public class Section {

	private Long id;
	private Station departure;
	private Station arrival;
	private Distance distance;

	public Section() {
	}

	public Section(final Long id, final Station departure, final Station arrival, final Distance distance) {
		this.id = id;
		this.departure = departure;
		this.arrival = arrival;
		this.distance = distance;
	}

	public boolean isSameDeparture(String name) {
		return departure.isSameName(name);
	}

	public boolean isSameArrival(String name) {
		return arrival.isSameName(name);
	}

	public String findDeparture() {
		return departure.getName();
	}

	public String findArrival() {
		return arrival.getName();
	}

	public boolean isShorter(int distance) {
		return this.distance.isShorter(distance);
	}

	public int subtractDistance(int distance) {
		return this.distance.subtractDistance(distance);
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

	public int getDistance() {
		return distance.getDistance();
	}

}
