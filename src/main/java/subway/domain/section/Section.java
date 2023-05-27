package subway.domain.section;

import subway.domain.line.Line;
import subway.domain.station.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Section {

    private final Line line;
    private final Station upward;
    private final Station downward;
    private final Distance distance;

    private Section(Line line, Station upward, Station downward, Distance distance) {
        validateCorrectStationInputs(upward, downward);
        validateSameStations(upward, downward);
        this.line = line;
        this.upward = upward;
        this.downward = downward;
        this.distance = distance;
    }

    public static Section of(Line line, Station upward, Station downward, int distance) {
        return new Section(line, upward, downward, Distance.from(distance));
    }

    public static Section of(Line line, Station upward, Station downward, Distance distance) {
        return new Section(line, upward, downward, distance);
    }

    public static Section ofCombinedTwoSection(Line line, Section upwardSection, Section downwardSection) {
        return new Section(
                line,
                upwardSection.upward,
                downwardSection.downward,
                upwardSection.distance.add(downwardSection.distance)
        );
    }

    private void validateCorrectStationInputs(Station upward, Station downward) {
        if (upward == null || downward == null) {
            throw new IllegalArgumentException("[ERROR] 구간을 구성할 역이 입력되지 않았습니다.");
        }
    }

    private void validateSameStations(Station upward, Station downward) {
        if (upward.equals(downward)) {
            throw new IllegalArgumentException(
                    String.format("[ERROR] 구간을 구성하는 역은 동일한 역일 수 없습니다. (입력값 : %s)", upward.getName())
            );
        }
    }

    public List<Section> splitSectionWithNewUpwardStation(Station newStation, int distance) {
        Distance newUpwardDistance = Distance.from(distance);
        Section newUpwardSection = of(
                line,
                upward,
                newStation,
                this.distance.subtract(newUpwardDistance)
        );
        Section newDownwardSection = Section.of(line, newStation, downward, distance);

        return new ArrayList<>(List.of(newUpwardSection, newDownwardSection));
    }

    public List<Section> splitSectionWithNewDownwardStation(Station newStation, int distance) {
        Distance newDownwardDistance = Distance.from(distance);
        Section newUpwardSection = Section.of(line, upward, newStation, distance);
        Section newDownwardSection = Section.of(
                line,
                newStation,
                downward,
                this.distance.subtract(newDownwardDistance)
        );

        return new ArrayList<>(List.of(newUpwardSection, newDownwardSection));
    }

    public boolean isComposed(Station upward, Station downward) {
        return this.upward.equals(upward) && this.downward.equals(downward);
    }

    public boolean isSameLine(Line line) {
        return this.line.equals(line);
    }

    public boolean isUpward(Station station) {
        return this.upward.equals(station);
    }

    public boolean isDownward(Station station) {
        return this.downward.equals(station);
    }

    public boolean hasGreaterDistanceThan(int distance) {
        return this.distance.isGreaterThan(Distance.from(distance));
    }

    public Line getLine() {
        return line;
    }

    public Station getUpward() {
        return upward;
    }

    public Station getDownward() {
        return downward;
    }

    public int getDistance() {
        return distance.getDistance();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section other = (Section) o;
        return Objects.equals(line, other.line)
                && Objects.equals(upward, other.upward)
                && Objects.equals(downward, other.downward)
                && Objects.equals(distance, other.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(line, upward, downward, distance);
    }
}
