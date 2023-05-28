package subway.domain.subwaymap;

import java.util.Objects;

public final class Section {

    private Long id;
    private Station upStation;
    private Station downStation;
    private int distance;

    Section() {

    }

    private Section(Long id, Station upStation, Station downStation, int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("구간의 거리는 0보다 커야 합니다.");
        }
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section of(final Long id, final Station upStation, final Station downStation, int distance) {
        return new Section(id, upStation, downStation, distance);
    }

    public static Section withNullId(final Station upStation, final Station downStation, int distance) {
        return new Section(null, upStation, downStation, distance);
    }

    public static Section of(final Station from, final Station to, final SectionDirection sectionDirection,
        final int distance) {
        if (sectionDirection == SectionDirection.DOWN) {
            return Section.withNullId(from, to, distance);
        }
        return Section.withNullId(to, from, distance);
    }

    public boolean containsStation(final Station station) {
        return upStation.equals(station) || downStation.equals(station);
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
        if (Objects.nonNull(id) && Objects.nonNull(section.id)) {
            return Objects.equals(id, section.id);
        }
        return distance == section.distance && Objects.equals(upStation, section.upStation)
            && Objects.equals(downStation, section.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, upStation, downStation, distance);
    }
}
