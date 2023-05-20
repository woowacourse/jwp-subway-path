package subway.domain;

import java.util.Objects;
import java.util.Optional;

public class Section {

    private final Long id;
    private final Station upStation;
    private final Station downStation;
    private final Line line;
    private final int distance;

    public Section(Long id, Station upStation, Station downStation, Line line, int distance) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.line = line;
        this.distance = distance;
    }

    public Section(Station upStation, Station downStation, Line line, int distance) {
        this(null, upStation, downStation, line, distance);
    }

    public Optional<Station> getStationWithGivenId(Long id) {
        if (Objects.equals(upStation.getId(), id)) {
            return Optional.of(upStation);
        }
        if (Objects.equals(downStation.getId(), id)) {
            return Optional.of(downStation);
        }
        return Optional.empty();
    }

    public boolean isUpStationGiven(Station station) {
        return upStation.isSame(station);
    }

    public boolean isDownStationGiven(Station station) {
        return downStation.isSame(station);
    }

    public int getReducedDistanceBy(int distance) {
        int reducedDistance = this.distance - distance;
        if (reducedDistance < 1) {
            throw new IllegalArgumentException("변경된 구간의 길이가 1 미만입니다");
        }
        return reducedDistance;
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

    public Line getLine() {
        return line;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Section section = (Section)o;

        if (distance != section.distance)
            return false;
        if (!Objects.equals(id, section.id))
            return false;
        if (!Objects.equals(upStation, section.upStation))
            return false;
        if (!Objects.equals(downStation, section.downStation))
            return false;
        return Objects.equals(line, section.line);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (upStation != null ? upStation.hashCode() : 0);
        result = 31 * result + (downStation != null ? downStation.hashCode() : 0);
        result = 31 * result + (line != null ? line.hashCode() : 0);
        result = 31 * result + distance;
        return result;
    }

    @Override
    public String toString() {
        return "Section{" +
            "id=" + id +
            ", upStation=" + upStation +
            ", downStation=" + downStation +
            ", line=" + line +
            ", distance=" + distance +
            '}';
    }
}
