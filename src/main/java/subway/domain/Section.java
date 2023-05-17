package subway.domain;

public class Section {
	private Long id;
	private final Line line;
	private Station upStation;
	private Station downStation;
	private Long distance;

	public Section(final Long id, final Line line, final Station upStation, final Station downStation, final Long distance) {
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
	public static Section of(final String lineName, final String upStationName, final String downStationName, final Long distance) {
		return new Section(new Line(lineName), new Station(upStationName), new Station(downStationName), distance);
	}

	public static Section of(final String upStation, final String downStation) {
		return new Section(null, new Station(upStation), new Station(downStation), null);
	}


	private void validateDuplication(final Station nowStation, final Station nextStation) {
		if (nowStation.equals(nextStation)) {
			throw new IllegalArgumentException("중복된 이름입니다. 다른 이름을 입력해주세요.");
		}
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

	public boolean validateMatchOne(final Section section) {
		return this.downStation.equals(section.downStation)
			|| this.upStation.equals(section.upStation);
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

	public boolean isNowStation(final Station station) {
		return station.equals(this.upStation);
	}

	public Section mergedSection(final Section section, final Station station) {
		final Long distance = this.distance + section.getDistance();

		if (this.downStation.equals(station)) {
			return new Section(this.line, this.upStation, section.downStation, distance);
		}
		return new Section(this.line, this.downStation, section.upStation, distance);
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