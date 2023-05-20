package subway.domain.core;

public class Section {
	private Long id;
	private final Line line;
	private Station upStation;
	private Station downStation;
	private Long distance;

	public Section(final Long id, final Line line, final Station upStation, final Station downStation,
		final Long distance) {
		this.id = id;
		this.line = line;
		this.upStation = upStation;
		this.downStation = downStation;
		this.distance = distance;
	}

	public Section(final Line line, final Station upStation, final Station downStation, final Long distance) {
		this.line = line;
		this.upStation = upStation;
		this.downStation = downStation;
		this.distance = distance;
	}

	public static Section of(final Line line, final String upStation, final String downStation, final Long distance) {
		return new Section(line, new Station(upStation), new Station(downStation), distance);
	}

	public static Section of(final String lineName, final String upStationName, final String downStationName,
		final Long distance) {
		return new Section(new Line(lineName), new Station(upStationName), new Station(downStationName), distance);
	}

	public static Section of(final String upStation, final String downStation) {
		return new Section(null, new Station(upStation), new Station(downStation), null);
	}

	public boolean validateEqualEndPoint(final Station upEndPoint, final Station downEndPoint) {
		return upEndPoint.equals(this.downStation) ^ downEndPoint.equals(this.upStation);
	}

	public boolean validateDuplicateSection(final Section newSection) {
		return this.downStation.equals(newSection.downStation)
			&& this.upStation.equals(newSection.upStation) ||
			this.upStation.equals(newSection.downStation)
				&& this.downStation.equals(newSection.upStation);
	}

	public void changeSection(final Section newSection) {
		changeStation(newSection);
		changeDistance(newSection);
	}

	private void changeStation(final Section newSection) {
		if (this.upStation.equals(newSection.upStation)) {
			this.upStation = newSection.downStation;
		}

		if (this.downStation.equals(newSection.downStation)) {
			this.downStation = newSection.upStation;
		}
	}

	private void changeDistance(final Section newSection) {
		distance -= newSection.distance;
	}

	public boolean hasStation(final Station station) {
		return upStation.equals(station) || downStation.equals(station);
	}

	public boolean isSameUpStation(final Station station) {
		return station.equals(this.upStation);
	}

	public Long getId() {
		return id;
	}

	public Line getLine() {
		return line;
	}

	public Station getUpStation() {
		return upStation;
	}

	public Station getDownStation() {
		return downStation;
	}

	public Long getDistance() {
		return distance;
	}
}
