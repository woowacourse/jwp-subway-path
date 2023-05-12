package subway.domain;

public class Section {

    private final Station station;
    private final int distance;

    public Section(final Station station, final int distance) {
        this.station = station;
        this.distance = distance;
    }

    public boolean isSameStation(Station station) {
        return this.station.equals(station);
    }

    public Station getStation() {
        return station;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "StationEdge{" +
                "destination=" + station +
                ", distance=" + distance +
                '}';
    }
}
