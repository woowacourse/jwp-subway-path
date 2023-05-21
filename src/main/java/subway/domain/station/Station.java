package subway.domain.station;

public class Station {

    private String name;

    public Station(final String name) {
        this.name = name;
    }

    public boolean isSame(final Station other) {
        return name.equals(other.name);
    }

    public void updateStationName(final Station station) {
        name = station.name;
    }

    public Station cloneStation() {
        return new Station(name);
    }

    public String getName() {
        return name;
    }
}
