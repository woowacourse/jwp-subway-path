package subway.domain;

import java.util.Objects;

public class Section {

    private final Long id;
    private final Station upStation;
    private final Station downStation;
    private final int distance;

    public Section(final Long id, final Station upStation, final Station downStation, final int distance) {
        validateDistance(distance);
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Section(final Station upStation, final Station downStation, final int distance) {
        this(null, upStation, downStation, distance);
    }

    private void validateDistance(final int distance) {
        if (distance < 0) {
            throw new IllegalArgumentException("거리는 음의 정수가 될 수 없습니다.");
        }
    }

    public boolean containStation(final Station station) {
        return station.equals(upStation) || station.equals(downStation);
    }

    public boolean isUpFinalStation() {
        return upStation == null;
    }

    public boolean isDownFinalStation() {
        return downStation == null;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section that = (Section) o;
        return distance == that.distance && upStation.equals(that.upStation) && downStation.equals(that.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upStation, downStation, distance);
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
}
