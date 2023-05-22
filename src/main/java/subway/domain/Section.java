package subway.domain;

import java.util.Objects;
import org.jgrapht.graph.DefaultWeightedEdge;
import subway.exception.ErrorCode;
import subway.exception.InvalidException;

public class Section extends DefaultWeightedEdge {
    public static final int MIN_DISTANCE = 0;

    private final Station upStation;
    private final Station downStation;
    private final int distance;

    public Section(Station upStation, Station downStation, int distance) {
        validateDifferentUpAndDown(upStation, downStation);
        validatePositiveDistance(distance);
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    private void validatePositiveDistance(int distance) {
        if (distance <= MIN_DISTANCE) {
            throw new InvalidException(ErrorCode.INVALID_NOT_POSITIVE_DISTANCE);
        }
    }

    private void validateDifferentUpAndDown(Station upStation, Station downStation) {
        if (upStation.equals(downStation)) {
            throw new InvalidException(ErrorCode.INVALID_SAME_UP_AND_DOWN_STATION);
        }
    }

    public Section getDividedSection(Section newSection) {
        if (upStation.equals(newSection.upStation)) {
            return new Section(newSection.downStation, downStation, distance - newSection.distance);
        }
        return new Section(upStation, newSection.upStation, distance - newSection.distance);
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Section section = (Section) o;
        return distance == section.distance && Objects.equals(upStation, section.upStation)
                && Objects.equals(downStation, section.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upStation, downStation, distance);
    }
}
