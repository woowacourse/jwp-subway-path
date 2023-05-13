package subway.business.domain;

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
        return this.distance - distanceToSubtract;
    }

    public boolean hasStation(Station station) {
        return upwardStation.equals(station) || downwardStation.equals(station);
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

    public int getDistance() {
        return distance;
    }
}
