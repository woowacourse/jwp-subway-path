package subway.domain.line;

import subway.domain.vo.Distance;

public class Section {

    private Long id;
    private final Station upStation;
    private final Station downStation;
    private Distance distance;

    public Section(Long id, Station upStation, Station downStation, Distance distance) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Section(Station upStation, Station downStation, Distance distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public boolean hasStation(Station station) {
        return isUpStation(station) || isDownStation(station);
    }

    public boolean isUpStation(Station station) {
        return upStation.equals(station);
    }

    public boolean isDownStation(Station station) {
        return downStation.equals(station);
    }

    public boolean hasSmallerDistanceThan(Distance distance) {
        return this.distance.isSmallerThan(distance);
    }

    public boolean isStationExistsAtDirection(Station station, Direction direction) {
        if (direction == Direction.UP) {
            return upStation.equals(station);
        }
        return downStation.equals(station);
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
