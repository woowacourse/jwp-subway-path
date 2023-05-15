package subway.business.domain;

public class Section {
    private final Station upwardStation;
    private final Station downwardStation;
    private final Distance distance;

    public Section(Station upwardStation, Station downwardStation, Distance distance) {
        this.upwardStation = upwardStation;
        this.downwardStation = downwardStation;
        this.distance = distance;
    }

    public Section(Station upwardStation, Station downwardStation, int distance) {
        this(upwardStation, downwardStation, new Distance(distance));
    }


    public int calculateRemainingDistance(int distanceToSubtract) {
        return this.distance.getDistance() - distanceToSubtract;
    }

    public boolean hasStation(Station station) {
        if (upwardStation.equals(station) || downwardStation.equals(station)) {
            return true;
        }
        return false;
    }

    public boolean isUpwardStation(Station station) {
        return this.upwardStation.equals(station);
    }

    public boolean isDownwardStation(Station station) {
        return this.downwardStation.equals(station);
    }

    public Station getUpwardStation() {
        return upwardStation;
    }

    public Station getDownwardStation() {
        return downwardStation;
    }

    public int getDistance() {
        return distance.getDistance();
    }
}
