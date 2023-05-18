package subway.business.domain.line;

import static subway.business.domain.direction.Direction.UPWARD;

import java.util.Objects;
import subway.business.domain.direction.Direction;

public class Section {
    private final Long id;
    private final Station upwardStation;
    private final Station downwardStation;
    private final int distance;

    public Section(Long id, Station upwardStation, Station downwardStation, int distance) {
        this.id = id;
        this.upwardStation = upwardStation;
        this.downwardStation = downwardStation;
        this.distance = distance;
    }

    public static Section createToSave(Station upwardStation, Station downwardStation, int distance) {
        return new Section(null, upwardStation, downwardStation, distance);
    }

    public int calculateRemainingDistance(int distanceToSubtract) {
        if (distance <= distanceToSubtract) {
            throw new IllegalArgumentException(String.format("추가할 구간의 거리가 기존 구간의 거리보다 크거나 같습니다. "
                    + "(입력한 거리 : %d / 기존 구간의 거리 : %d)", distanceToSubtract, distance)
            );
        }
        return this.distance - distanceToSubtract;
    }

    public boolean hasStationNameOf(Station station) {
        return upwardStation.hasNameOf(station.getName()) || downwardStation.hasNameOf(station.getName());
    }

    public boolean isUpwardStation(Station station) {
        return this.upwardStation.equals(station);
    }

    public boolean isDownwardStation(Station station) {
        return this.downwardStation.equals(station);
    }

    public boolean hasStationOfDirection(Station station, Direction direction) {
        if (direction.equals(UPWARD)) {
            return this.upwardStation.hasNameOf(station.getName());
        }
        return this.downwardStation.hasNameOf(station.getName());
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

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this.id == null) {
            throw new IllegalStateException("ID가 존재하지 않는 Section을 기준으로 비교했습니다.");
        }
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Section section = (Section) o;
        if (section.id == null) {
            throw new IllegalStateException("ID가 존재하지 않는 Section을 인자로 넣어 비교했습니다.");
        }
        return Objects.equals(id, section.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
