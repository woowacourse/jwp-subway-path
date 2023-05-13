package subway.domain;

import subway.exception.ApiIllegalArgumentException;

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
        validateDistance(distance);
        validateStations(upStation, downStation);
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    private void validateDistance(int distance) {
        if (distance <= 0) {
            throw new ApiIllegalArgumentException("구간의 거리는 1 이상이여야합니다.");
        }
    }

    private void validateStations(final Station upStation, final Station downStation) {
        if (upStation.equals(downStation)) {
            throw new ApiIllegalArgumentException("상행역과 하행역은 달라야합니다.");
        }
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Section section = (Section) o;

        if (distance != section.distance) {
            return false;
        }
        if (id != null ? !id.equals(section.id) : section.id != null) {
            return false;
        }
        if (upStation != null ? !upStation.equals(section.upStation) : section.upStation != null) {
            return false;
        }
        return downStation != null ? downStation.equals(section.downStation) : section.downStation == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (upStation != null ? upStation.hashCode() : 0);
        result = 31 * result + (downStation != null ? downStation.hashCode() : 0);
        result = 31 * result + distance;
        return result;
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

    public boolean contains(Station station) {
        return upStation.equals(station) || downStation.equals(station);
    }
}
