package subway.domain.section;

import subway.domain.station.Station;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class Section {

    private final Long id;
    private final Station leftStation;
    private final Station rightStation;
    private final Distance distance;

    public Section(final Station leftStation, final Station rightStation, final int distance) {
        this(null, leftStation, rightStation, distance);
    }

    public Section(final Long id, final Station leftStation, final Station rightStation, final int distance) {
        this.id = id;
        validateBothStationsIsNull(leftStation, rightStation);
        this.leftStation = leftStation;
        this.rightStation = rightStation;
        this.distance = new Distance(distance);
    }

    private void validateBothStationsIsNull(final Station leftStation, final Station rightStation) {
        if (Objects.isNull(leftStation) || Objects.isNull(rightStation)) {
            throw new IllegalArgumentException("비어 있는 역이 존재하면 안됩니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Station getLeftStation() {
        return leftStation;
    }

    public Station getRightStation() {
        return rightStation;
    }

    public Distance getDistance() {
        return distance;
    }

    public Queue<Section> split(Station newStation, int leftDistance, int rightDistance) {
        if (leftDistance + rightDistance != distance.getDistance()) {
            throw new IllegalArgumentException("분할된 역간 거리의 합은 기존 역간 거리와 같아야 합니다.");
        }

        Queue<Section> splitSections = new LinkedList<>();
        splitSections.add(new Section(leftStation, newStation, leftDistance));
        splitSections.add(new Section(newStation, rightStation, rightDistance));

        return splitSections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Section)) return false;
        Section section = (Section) o;
        return Objects.equals(id, section.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
