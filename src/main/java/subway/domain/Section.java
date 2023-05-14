package subway.domain;

import java.util.List;
import java.util.Objects;

public class Section {

    private final Long id;
    private final Station upwardStation;
    private final Station downwardStation;
    private final Distance distance;

    private Section(Long id, Station upwardStation, Station downwardStation, Distance distance) {
        validateSameStations(upwardStation, downwardStation);
        this.id = id;
        this.upwardStation = upwardStation;
        this.downwardStation = downwardStation;
        this.distance = distance;
    }

    public static Section createEmpty() {
        return new Section(null, Station.createEmpty(), Station.createEmpty(), Distance.createEmpty());
    }

    public static Section of(Long id, Station upward, Station downward, Integer distance) {
        return new Section(id, upward, downward, Distance.from(distance));
    }

    public static Section of(Station upward, Station downward, Integer distance) {
        return new Section(null, upward, downward, Distance.from(distance));
    }

    private void validateSameStations(Station upward, Station downward) {
        if (upward.isEmpty() && downward.isEmpty()) {
            return;
        }
        if (upward.equals(downward)) {
            throw new IllegalArgumentException("[ERROR] 구간을 구성하는 역은 동일한 역일 수 없습니다.");
        }
        if (downward.equals(upward)) {
            throw new IllegalArgumentException("[ERROR] 구간을 구성하는 역은 동일한 역일 수 없습니다.");
        }
    }

    public List<Section> splitByStation(final Station middleStation, Integer upwardDistance, Integer downwardDistance) {
        if (!isEmptySection() && !isSameDistance(upwardDistance + downwardDistance)) {
            throw new IllegalArgumentException("[ERROR] 구간 거리가 보존되지 않습니다.");
        }
        Section upwardSection = Section.of(this.upwardStation, middleStation, upwardDistance);
        Section downwardSection = Section.of(middleStation, this.downwardStation, downwardDistance);
        return List.of(upwardSection, downwardSection);
    }

    public boolean isEndOfUpward() {
        return this.upwardStation.isEmpty() && this.distance.isEmpty();
    }

    public boolean isEndOfDownward() {
        return this.downwardStation.isEmpty() && this.distance.isEmpty();
    }

    public boolean isSameDistance(int distance) {
        return this.distance.equals(Distance.from(distance));
    }

    public boolean isEmptySection() {
        return isEndOfUpward() || isEndOfDownward();
    }

    public boolean isUpwardStation(Station station) {
        return this.upwardStation.equals(station);
    }

    public boolean isDownwardStation(Station station) {
        return this.downwardStation.equals(station);
    }

    public Long getId() {
        return id;
    }

    public Station getUpwardStation() {
        return upwardStation;
    }

    public Station getDownwardStation() {
        return downwardStation;
    }

    public Integer getDistance() {
        return distance.getDistance();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return Objects.equals(upwardStation, section.upwardStation)
                && Objects.equals(downwardStation, section.downwardStation)
                && Objects.equals(distance, section.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upwardStation, downwardStation, distance);
    }

}
