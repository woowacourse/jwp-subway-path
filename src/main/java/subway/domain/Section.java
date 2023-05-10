package subway.domain;

public class Section {
    private Station startStation;
    private Station endStation;
    private int distance;

    public Section(Station startStation, Station endStation, int distance) {
        this.startStation = startStation;
        this.endStation = endStation;
        this.distance = distance;
    }

    public boolean hasStation(Station station) {
        return startStation.equals(station) || endStation.equals(station);
    }

    public boolean hasStartStation(Station station) {
        return startStation.equals(station);
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
