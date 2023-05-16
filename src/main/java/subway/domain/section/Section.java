package subway.domain.section;

import subway.domain.station.Station;

import java.util.Objects;

public class Section {

    private final Long id;
    private final Station leftStation;
    private final Station rightStation;
    private final Distance distance;

    public Section(final Long id, final Station leftStation, final Station rightStation, final int distance) {
        this.id = id;
        validateBothStationsIsNull(leftStation, rightStation);
        this.leftStation = leftStation;
        this.rightStation = rightStation;
        this.distance = new Distance(distance);
    }

    public Section(final Station leftStation, final Station rightStation, final int distance) {
        this(null, leftStation, rightStation, distance);
    }

    private void validateBothStationsIsNull(final Station leftStation, final Station rightStation) {
        if (Objects.isNull(leftStation) || Objects.isNull(rightStation)) {
            throw new IllegalArgumentException("비어 있는 역이 존재하면 안됩니다.");
        }
    }

    public boolean isShort(int distance) {
        return distance >= this.distance.getDistance();
    }

    public int calculateDistance(int distance) {
        return this.distance.getDistance() - distance;
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
}
