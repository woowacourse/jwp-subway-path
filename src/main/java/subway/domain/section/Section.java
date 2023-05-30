package subway.domain.section;

import subway.exception.SubwayIllegalArgumentException;
import subway.domain.station.Station;

public class Section {

    private final Long id;
    private final Station upStation;
    private final Station downStation;
    private final int distance;

    public Section(final Station upStation, final Station downStation,
            final int distance) {
        this(null, upStation, downStation, distance);
    }

    public Section(final Long id, final Station upStation, final Station downStation,
            final int distance) {
        validateStations(upStation, downStation);
        validateDistance(distance);
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    private void validateStations(final Station upStation, final Station downStation) {
        if (upStation.equals(downStation)) {
            throw new SubwayIllegalArgumentException("상행역과 하행역은 달라야합니다.");
        }
    }

    private void validateDistance(final int distance) {
        if (distance <= 0) {
            throw new SubwayIllegalArgumentException("거리는 0보다 커야합니다.");
        }
    }

    public boolean contains(final Station station) {
        return upStation.equals(station) || downStation.equals(station);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Section section = (Section) o;

        return id != null ? id.equals(section.id) : section.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", upStation=" + upStation +
                ", downStation=" + downStation +
                ", distance=" + distance +
                '}';
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

    public int getDistance() {
        return distance;
    }
}
