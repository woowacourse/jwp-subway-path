package subway.domain;

import java.util.Objects;

public class SectionDomain {

    private final Long id;
    private final Distance distance;
    private final boolean isStart;
    private final StationDomain upStation;
    private final StationDomain downStation;

    public SectionDomain(
            final Distance distance,
            final boolean isStart,
            final StationDomain upStation,
            final StationDomain downStation
    ) {
        this(null, distance, isStart, upStation, downStation);
    }

    private SectionDomain(
            final Long id,
            final Distance distance,
            final boolean isStart,
            final StationDomain upStation,
            final StationDomain downStation
    ) {
        validate(upStation, downStation, distance);
        this.id = id;
        this.distance = distance;
        this.isStart = isStart;
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public static SectionDomain from(
            final Long id,
            final Distance distance,
            final boolean isStart,
            final StationDomain upStation,
            final StationDomain downStation
    ) {
        return new SectionDomain(id, distance, isStart, upStation, downStation);
    }

    public static SectionDomain notStartFrom(
            final StationDomain upStation,
            final StationDomain downStation,
            final Distance distance
    ) {
        return new SectionDomain(distance, false, upStation, downStation);
    }

    public static SectionDomain startFrom(
            final StationDomain upStation,
            final StationDomain downStation,
            final Distance distance
    ) {
        return new SectionDomain(distance, true, upStation, downStation);
    }

    private void validate(final StationDomain upStation, final StationDomain downStation, final Distance distance) {
        if (Objects.isNull(upStation)) {
            throw new IllegalArgumentException("상행역은 null 일 수 없습니다.");
        }
        if (Objects.isNull(downStation)) {
            throw new IllegalArgumentException("하행역은 null 일 수 없습니다.");
        }
        if (Objects.isNull(distance)) {
            throw new IllegalArgumentException("거리는 null 일 수 없습니다.");
        }
    }

    public boolean isBaseStationExist(final SectionDomain otherSection) {
        return upStation.equals(otherSection.upStation)
                || upStation.equals(otherSection.downStation)
                || downStation.equals(otherSection.upStation)
                || downStation.equals(otherSection.downStation);
    }

    public boolean isSameUpStationBy(final StationDomain otherStation) {
        return Objects.equals(upStation, otherStation);
    }

    public boolean isSameDownStationBy(final StationDomain otherStation) {
        return Objects.equals(downStation, otherStation);
    }

    public boolean isDistanceLessThan(final SectionDomain otherStation) {
        return !distance.isOver(otherStation.distance);
    }

    public Long getId() {
        return id;
    }

    public StationDomain getUpStation() {
        return upStation;
    }

    public StationDomain getDownStation() {
        return downStation;
    }

    public Distance getDistance() {
        return distance;
    }

    public boolean isStart() {
        return isStart;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SectionDomain that = (SectionDomain) o;
        return isStart == that.isStart && Objects.equals(id, that.id) && Objects.equals(distance, that.distance) && Objects.equals(upStation, that.upStation) && Objects.equals(downStation, that.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, distance, isStart, upStation, downStation);
    }

    @Override
    public String toString() {
        return "SectionDomain{" +
                "id=" + id +
                ", distance=" + distance +
                ", isStart=" + isStart +
                ", upStation=" + upStation +
                ", downStation=" + downStation +
                '}';
    }
}
