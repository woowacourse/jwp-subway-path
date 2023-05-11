package subway.domain;

import java.util.Objects;

public class Section {

    private final Long id;
    private final Station upStation;
    private final Station downStation;
    private final Line line;
    private final Distance distance;
    private final boolean isStart;

    public Section(final Station upStation, final Station downStation, final Line line, final Distance distance, final boolean isStart) {
        this(null, upStation, downStation, line, distance, isStart);
    }

    public Section(final Long id, final Station upStation, final Station downStation, final Line line, final Distance distance, final boolean isStart) {
        validate(upStation, downStation, line, distance);
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.line = line;
        this.distance = distance;
        this.isStart = isStart;
    }

    private void validate(final Station upStation, final Station downStation, final Line line, final Distance distance) {
        if (Objects.isNull(upStation)) {
            throw new IllegalArgumentException("다음역은 null 일 수 없습니다.");
        }
        if (Objects.isNull(downStation)) {
            throw new IllegalArgumentException("다음역은 null 일 수 없습니다.");
        }
        if (Objects.isNull(line)) {
            throw new IllegalArgumentException("노선은 null 일 수 없습니다.");
        }
        if (Objects.isNull(distance)) {
            throw new IllegalArgumentException("거리는 null 일 수 없습니다.");
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

    public Line getLine() {
        return line;
    }

    public Distance getDistance() {
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
        return Objects.equals(id, section.id) && Objects.equals(this.downStation, section.downStation) && Objects.equals(line, section.line) && Objects.equals(distance, section.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, downStation, line, distance);
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", nextStation=" + downStation +
                ", line=" + line +
                ", distance=" + distance +
                '}';
    }
}
