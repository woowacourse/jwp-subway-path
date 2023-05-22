package subway.application.core.domain;

import subway.application.core.exception.SectionConnectException;

import java.util.Objects;

public class Section {

    private final Station upBound;
    private final Station downBound;
    private final Distance distance;

    public Section(Station upBound, Station downBound, Distance distance) {
        this.upBound = upBound;
        this.downBound = downBound;
        this.distance = distance;
    }

    public boolean hasAnySameStationWith(Section targetSection) {
        return hasStationOf(targetSection.upBound) || hasStationOf(targetSection.downBound);
    }

    private boolean hasStationOf(Station station) {
        return upBound.equals(station) || downBound.equals(station);
    }

    public boolean isDistanceBiggerOrEqualThan(Section target) {
        return distance.isBiggerThan(target.distance) || distance.equals(target.distance);
    }

    public boolean overlaps(Section target) {
        return target.downBound.equals(downBound) || target.upBound.equals(upBound);
    }

    public boolean containsDownBoundOf(Section target) {
        return downBound.equals(target.downBound) || upBound.equals(target.downBound);
    }

    public boolean containsUpBoundOf(Section target) {
        return downBound.equals(target.upBound) || upBound.equals(target.upBound);
    }

    public Section makeConnectionTo(Section target) {
        if (containsUpBoundOf(target)) {
            return new Section(target.downBound, downBound, distance.sub(target.distance));
        }
        return new Section(upBound, target.upBound, distance.sub(target.distance));
    }

    public boolean hasUpBound(Station target) {
        return upBound.equals(target);
    }

    public boolean hasDownBound(Station target) {
        return downBound.equals(target);
    }

    public Section merge(Section target) {
        if (!downBound.equals(target.upBound)) {
            throw new SectionConnectException();
        }

        return new Section(upBound, target.downBound, distance.add(target.distance));
    }

    public Station getUpBound() {
        return upBound;
    }

    public Station getDownBound() {
        return downBound;
    }

    public Distance getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return upBound.equals(section.upBound) && downBound.equals(section.downBound);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upBound, downBound);
    }
}
