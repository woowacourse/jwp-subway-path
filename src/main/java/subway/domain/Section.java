package subway.domain;

public class Section {
    private Long lineId;
    private Station upStation;
    private Station downStation;
    private Long distance;

    public Section(final Station upStation, final Station downStation, final Long distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Section(final Long lineId, final Station upStation, final Station downStation, final Long distance) {
        validateDuplication(upStation, downStation);
        this.lineId = lineId;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }


    public void validateDuplication(final Station upStation, final Station downStation) {
        if (upStation.equals(downStation)) {
            throw new IllegalArgumentException("중복된 역입니다. 다른 역을 입력해주세요.");
        }
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

    public boolean validateDistance(final Section newSection) {
        return this.distance <= newSection.distance;
    }

    public boolean hasStation(final Station station) {
        return upStation.equals(station) || downStation.equals(station);
    }

    public Section mergedSection(final Section section, final Station station) {
        final Long distance = this.distance + section.distance;

        if (this.downStation.equals(station)) {
            return new Section(this.upStation, section.downStation, distance);
        }
        return new Section(this.downStation, section.upStation, distance);
    }

    public boolean validateEqualEndPoint(final Station upEndPoint, final Station downEndPoint) {
        return upEndPoint.equals(this.downStation) ^ downEndPoint.equals(this.upStation);
    }

    public boolean isNowStation(final Station station) {
        return station.equals(this.upStation);
    }

    public Long getLineId() {
        return lineId;
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
