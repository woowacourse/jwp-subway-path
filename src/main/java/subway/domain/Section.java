package subway.domain;

import java.util.List;
import java.util.Objects;

public class Section {

    private final Long id;
    private final Station upwardStation;
    private final Station downwardStation;
    private final Distance distance;

    private Section(final Long id, final Station upwardStation, final Station downwardStation, final Distance distance) {
        validateSameStations(upwardStation, downwardStation);
        this.id = id;
        this.upwardStation = upwardStation;
        this.downwardStation = downwardStation;
        this.distance = distance;
    }

    public static Section createEmpty() {
        return new Section(null, Station.createEmpty(), Station.createEmpty(), Distance.createEmpty());
    }

    public static Section of(final Long id, final Station upward, final Station downward, final Integer distance) {
        return new Section(id, upward, downward, Distance.from(distance));
    }

    public static Section of(final Station upward, final Station downward, final Integer distance) {
        return new Section(null, upward, downward, Distance.from(distance));
    }

    private void validateSameStations(final Station upward, final Station downward) {
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

    public List<Section> splitByStation(final Station middleStation, final Integer upwardDistance, final Integer downwardDistance) {
        validateDistanceLessThanZero(upwardDistance);
        validateDistanceLessThanZero(downwardDistance);
        if (!isEmptySection() && !isSameDistance(upwardDistance + downwardDistance)) {
            throw new IllegalArgumentException("[ERROR] 구간 거리가 보존되지 않습니다.");
        }
        Section upwardSection = Section.of(this.upwardStation, middleStation, upwardDistance);
        Section downwardSection = Section.of(middleStation, this.downwardStation, downwardDistance);
        return List.of(upwardSection, downwardSection);
    }

    private void validateDistanceLessThanZero(final Integer distance) {
        if (distance == null) {
            return;
        }
        if (distance <= 0) {
            throw new IllegalArgumentException("[ERROR] 기존 구간에 역을 삽입한 결과로 거리가 0 이하인 구간이 발생하게 됩니다.");
        }
    }

    public boolean isEndOfUpward() {
        return this.upwardStation.isEmpty() && this.distance.isEmpty();
    }

    public boolean isEndOfDownward() {
        return this.downwardStation.isEmpty() && this.distance.isEmpty();
    }

    public boolean isSameDistance(final int distance) {
        return this.distance.equals(Distance.from(distance));
    }

    public boolean isEmptySection() {
        return isEndOfUpward() || isEndOfDownward();
    }

    public boolean isUpwardStation(final Station station) {
        return this.upwardStation.equals(station);
    }

    public boolean isDownwardStation(final Station station) {
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
