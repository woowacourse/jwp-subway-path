package subway.domain;

import java.util.Objects;

public class Section {

    private final Long id;
    private final Station upward;
    private final Station downward;
    private final Distance distance;
    private final Line line;

    private Section(Long id, Station upward, Station downward, Distance distance, Line line) {
        validateCorrectStationInputs(upward, downward);
        validateSameStations(upward, downward);
        this.id = id;
        this.upward = upward;
        this.downward = downward;
        this.distance = distance;
        this.line = line;
    }

    public static Section of(long id, Station upward, Station downward, int distance, Line line) {
        return new Section(id, upward, downward, Distance.from(distance), line);
    }

    public static Section of(Station upward, Station downward, int distance, Line line) {
        return new Section(null, upward, downward, Distance.from(distance), line);
    }

    public static Section ofEmptyUpwardSection(Station downward, Line line) {
        return new Section(null, makeEmptyUpwardStation(line), downward, Distance.from(0), line);
    }

    private static Station makeEmptyUpwardStation(Line line) {
        return Station.from(line.getName() + " 상행 종착역");
    }

    public static Section ofEmptyDownwardSection(Station upward, Line line) {
        return new Section(null, upward, makeEmptyDownwardStation(line), Distance.from(0), line);
    }

    private static Station makeEmptyDownwardStation(Line line) {
        return Station.from(line.getName() + " 하행 종착역");
    }

    private void validateCorrectStationInputs(Station upward, Station downward) {
        if (upward == null || downward == null) {
            throw new IllegalArgumentException("[ERROR] 구간을 구성할 역이 입력되지 않았습니다.");
        }
    }

    private void validateSameStations(Station upward, Station downward) {
        if (upward.equals(downward)) {
            throw new IllegalArgumentException("[ERROR] 구간을 구성하는 역은 동일한 역일 수 없습니다.");
        }
    }

    public boolean isComposed(Station upward, Station downward) {
        return this.upward.equals(upward) && this.downward.equals(downward);
    }

    public boolean isSameDistance(int distance) {
        return this.distance.equals(Distance.from(distance));
    }

    public Long getId() {
        return id;
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

    public Line getLine() {
        return line;
    }

    public boolean isSameLine(Line line) {
        return this.line.equals(line);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return Objects.equals(upward, section.upward)
                && Objects.equals(downward, section.downward)
                && Objects.equals(distance, section.distance)
                && Objects.equals(line, section.line);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upward, downward, distance, line);
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", upward=" + upward +
                ", downward=" + downward +
                ", distance=" + distance +
                ", line=" + line +
                '}';
    }

    public boolean isUpward(Station station) {
        return this.upward.equals(station);
    }

    public boolean isDownward(Station station) {
        return this.downward.equals(station);
    }
}
