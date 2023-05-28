package subway.domain;

import java.util.Objects;
import subway.exception.ErrorMessage;
import subway.exception.InvalidException;

public class Section {
    private final Station upStation;
    private final Station downStation;
    private final int distance;

    public Section(final Station upStation, final Station downStation, final int distance) {
        validateDifferentUpAndDown(upStation, downStation);
        validatePositiveDistance(distance);

        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    private static void validatePositiveDistance(final int distance) {
        if (distance <= 0) {
            throw new InvalidException(ErrorMessage.INVALID_NOT_POSITIVE_DISTANCE);
        }
    }

    private static void validateDifferentUpAndDown(final Station upStation, final Station downStation) {
        if (upStation.getId() == null || downStation.getId() == null) {
            return;
        }
        if (upStation.equals(downStation)) {
            throw new InvalidException(ErrorMessage.INVALID_SAME_UP_AND_DOWN_STATION);
        }
    }

    public Section getDividedSection(final Section newSection) {
        validateDistance(newSection.distance);

        if (upStation.equals(newSection.upStation)) {
            return new Section(newSection.downStation, downStation, distance - newSection.distance);
        }
        return new Section(upStation, newSection.upStation, distance - newSection.distance);
    }

    private void validateDistance(final int distance) {
        if (this.distance <= distance) {
            throw new InvalidException(ErrorMessage.INVALID_DISTANCE);
        }
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
        Section section = (Section) o;
        return distance == section.distance && Objects.equals(upStation, section.upStation)
                && Objects.equals(downStation, section.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upStation, downStation, distance);
    }
}
