package subway.domain.section;

import subway.domain.station.Station;

public class Section {

    private final Long id;
    private final Station upStation;
    private final Station downStation;
    private final Distance distance;

    public Section(final Station upStation, final Station downStation, final Distance distance) {
        this(null, upStation, downStation, distance);
    }

    public Section(final Long id, final Station upStation, final Station downStation, final Distance distance) {
        validateStation(upStation);
        validateStation(downStation);
        validateDifferentStation(upStation, downStation);
        validateDistance(distance);

        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }


    private void validateStation(final Station station) {
        if (station == null) {
            throw new IllegalArgumentException("구간에 역은 필수로 존재해야 합니다.");
        }
    }

    private void validateDifferentStation(final Station upStation, final Station downStation) {
        if (upStation.isSameStation(downStation) || downStation.isSameStation(upStation)) {
            throw new IllegalArgumentException("한 구간은 서로 다른 역으로 이루어져야 합니다.");
        }
    }

    private void validateDistance(final Distance distance) {
        if (distance == null) {
            throw new IllegalArgumentException("구간은 역 사이에 거리가 필수로 존재해야합니다.");
        }
    }

    public boolean isSameUpStation(final Section other) {
        return this.upStation.isSameStation(other.upStation);
    }

    public boolean isSameUpStationByFlip(final Section other) {
        return this.upStation.isSameStation(other.downStation);
    }

    public boolean isSameDownStationByFlip(final Section other) {
        return this.downStation.isSameStation(other.upStation);
    }

    public boolean isSameUpStation(final Station other) {
        return this.upStation.isSameStation(other);
    }

    public boolean isSameDownStation(final Section other) {
        return this.downStation.isSameStation(other.downStation);
    }

    public boolean isSameDownStation(final Station other) {
        return this.downStation.isSameStation(other);
    }

    public boolean isAssemblableOnFront(final Section other) {
        return this.upStation.isSameStation(other.downStation);
    }

    public boolean isAssemblableOnBack(final Section other) {
        return this.downStation.isSameStation(other.upStation);
    }

    public Section createDownToDownSection(final Section other) {
        return new Section(
                other.downStation,
                this.downStation,
                this.distance.subtract(other.distance)
        );
    }

    public Section createUpToUpSection(final Section other) {
        return new Section(
                other.upStation,
                this.upStation,
                other.distance.subtract(this.distance)
        );
    }

    public Section addSection(final Section other) {
        return new Section(
                this.upStation,
                other.downStation,
                this.distance.add(other.distance)
        );
    }

    public Long getId() {
        return id;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Distance getDistance() {
        return distance;
    }
}
