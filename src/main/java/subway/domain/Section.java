package subway.domain;

public class Section {
    private Station startStation;
    private Station endStation;
    private int distance;

    public Section(Station startStation, Station endStation, int distance) {
        validateDistance(distance);
        this.startStation = startStation;
        this.endStation = endStation;
        this.distance = distance;
    }

    private void validateDistance(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("구간의 길이는 1 이상 이어야 합니다.");
        }
    }

    public boolean hasEndStation(Station station) {
        return endStation.equals(station);
    }

    public void updateEndStation(Station station) {
        endStation = station;
    }

    public void updateStartStation(Station station) {
        startStation = station;
    }

    public Station getStartStation() {
        return startStation;
    }

    public Station getEndStation() {
        return endStation;
    }

    public int getDistance() {
        return distance;
    }
}
