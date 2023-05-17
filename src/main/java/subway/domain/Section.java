package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Section {
    public static final Section EMPTY_SECTION = new Section(Station.EMPTY_STATION, Station.EMPTY_STATION, Distance.EMPTY_DISTANCE);

    private final Long id;
    private final Station upStation;
    private final Station downStation;
    private final Distance distance;

    private Section(final Station upStation, final Station downStation, final Distance distance) {
        this(null, upStation, downStation, distance);
    }

    public Section(final Long id, final Station upStation, final Station downStation, final Distance distance) {
        validate(upStation, downStation, distance);
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section of(final Station upStation, final Station downStation, final Distance distance) {
        if ((upStation == null || upStation.equals(Station.EMPTY_STATION)) ||
                (downStation == null || downStation.equals(Station.EMPTY_STATION))) {
            return EMPTY_SECTION;
        }
        return new Section(upStation, downStation, distance);
    }

    private void validate(final Station upStation, final Station downStation, final Distance distance) {
        if (Objects.isNull(upStation)) {
            throw new IllegalArgumentException("다음역은 null 일 수 없습니다.");
        }
        if (Objects.isNull(downStation)) {
            throw new IllegalArgumentException("다음역은 null 일 수 없습니다.");
        }
        if (Objects.isNull(distance)) {
            throw new IllegalArgumentException("거리는 null 일 수 없습니다.");
        }
    }

    public boolean isPossibleDivideTo(final Section upSection, final Section downSection) {
        return this.upStation.equals(upSection.upStation)
                && this.downStation.equals(downSection.downStation)
                && this.distance.equalToSumOf(upSection.distance, downSection.distance);
    }

    public Section mergeWith(final Section other) {
        if (this.downStation.equals(other.upStation)) {
            return new Section(this.upStation, other.downStation, this.distance.sum(other.distance));
        }
        if (this.upStation.equals(other.downStation)) {
            return new Section(other.upStation, this.downStation, other.distance.sum(this.distance));
        }
        throw new IllegalArgumentException("합치는 구간이 연결된 구간이 아닙니다.");
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Section section = (Section) o;
        return Objects.equals(id, section.id)
                && Objects.equals(upStation, section.upStation)
                && Objects.equals(downStation, section.downStation)
                && Objects.equals(distance, section.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, upStation, downStation, distance);
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

    public List<Section> divide(final Section other) {
        if (other.upStation.equals(this.upStation) && other.downStation.equals(this.downStation)) {
            throw new IllegalArgumentException("나누는 구간과 역이 같습니다.");
        }
        if (!other.upStation.equals(this.upStation) && !other.downStation.equals(this.downStation)) {
            throw new IllegalArgumentException("구간을 나눌 수 없는 역입니다.");
        }
        if (other.distance.isSameOrOverThan(this.distance)) {
            throw new IllegalArgumentException("나누는 구간의 길이보다 깁니다.");
        }
        List<Section> sections = new ArrayList<>();
        sections.add(other);
        if (other.upStation.equals(this.upStation)) {
            sections.add(Section.of(other.downStation, this.downStation, this.distance.minus(other.distance)));
        }
        if (other.downStation.equals(this.downStation)) {
            sections.add(Section.of(this.upStation, other.upStation, this.distance.minus(other.distance)));
        }
        return sections;
    }
}
