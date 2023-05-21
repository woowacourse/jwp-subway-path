package subway.domain;

import java.util.Objects;

public class Section {

    private final Long id;
    private final Line line;
    private final Station upStation;
    private final Station downStation;
    private final Distance distance;

    public Section(Long id, final Line line, final Station upStation, final Station downStation, final Distance distance) {
        this.id = id;
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Section(final Line line, final Station upStation, final Station downStation, final Distance distance) {
        this(null, line, upStation, downStation, distance);
    }
    
    public static Section combineSection(final Section upSection, final Section downSection) {
        return new Section(
                upSection.line,
                upSection.upStation,
                downSection.downStation,
                upSection.distance.add(downSection.distance)
        );
    }

    public Section reverseDirection() {
        return new Section(id, line, downStation, upStation, distance);
    }

    public boolean isReverseDirection(final Section otherSection) {
        return upStation.equals(otherSection.downStation) && downStation.equals(otherSection.upStation);
    }

    public boolean isSameDirection(final Section otherSection) {
        return upStation.equals(otherSection.upStation) && downStation.equals(otherSection.downStation);
    }

    public boolean isSameLine(final Section otherSection) {
        return line.equals(otherSection.line);
    }

    public boolean isSameUpStation(final Station otherStation) {
        return this.upStation.equals(otherStation);
    }

    public boolean isSameDownStation(final Station otherStation) {
        return this.downStation.equals(otherStation);
    }

    public boolean isSameStations(final Station upStation, final Station downStation) {
        return isSameUpStation(upStation) && isSameDownStation(downStation);
    }

    public boolean isSameDistance(final Section upSection, final Section downSection) {
        Distance totalDistance = upSection.distance.add(downSection.distance);
        return this.distance.equals(totalDistance);
    }

    public boolean isForkRoad(final Section otherSection) {
        return this.line.equals(otherSection.line) && this.upStation.equals(otherSection.upStation);
    }

    public boolean hasSameStation(final Station otherStation) {
        return this.upStation.equals(otherStation) || this.downStation.equals(otherStation);
    }

    public Long getId() {
        return id;
    }

    public Line getLine() {
        return line;
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
        return Objects.equals(line, section.line) && Objects.equals(upStation, section.upStation) && Objects.equals(downStation, section.downStation) && Objects.equals(distance, section.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(line, upStation, downStation, distance);
    }
}
