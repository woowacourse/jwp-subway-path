package subway.domain;

import java.util.Objects;

public class Section {

    private final Long id;
    private final Station upStation;
    private final Station downStation;
    private final Distance distance;

    public Section(final Station upStation, final Station downStation, final Distance distance) {
        this(null, upStation, downStation, distance);
    }

    public Section(final Long id, final Station upStation, final Station downStation, final Distance distance) {
        validate(upStation, downStation, distance);
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
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
}
