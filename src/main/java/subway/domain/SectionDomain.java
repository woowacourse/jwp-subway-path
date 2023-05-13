package subway.domain;

import java.util.Objects;

public class SectionDomain {

    private final Long id;
    private final StationDomain upStation;
    private final StationDomain downStation;
    private final Distance distance;
    private final boolean isStart;

    protected SectionDomain(final StationDomain upStation, final StationDomain downStation, final Distance distance, final boolean isStart) {
        this(null, upStation, downStation, distance, isStart);
    }

    protected SectionDomain(final Long id, final StationDomain upStation, final StationDomain downStation, final Distance distance, final boolean isStart) {
        validate(upStation, downStation, distance);
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.isStart = isStart;
    }

    public static SectionDomain from(
            final Long id,
            final StationDomain upStation,
            final StationDomain downStation,
            final Distance distance,
            final boolean isStart
    ) {
        return new SectionDomain(id, upStation, downStation, distance, isStart);
    }

    public static SectionDomain notStartFrom(
            final StationDomain upStation,
            final StationDomain downStation,
            final Distance distance
    ) {
        return new SectionDomain(upStation, downStation, distance, false);
    }

    public static SectionDomain startFrom(
            final StationDomain upStation,
            final StationDomain downStation,
            final Distance distance
    ) {
        return new SectionDomain(upStation, downStation, distance, true);
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
        return isStart == that.isStart && Objects.equals(id, that.id) && Objects.equals(upStation, that.upStation) && Objects.equals(downStation, that.downStation) && Objects.equals(distance, that.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, upStation, downStation, distance, isStart);
    }
}
