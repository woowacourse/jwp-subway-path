package subway.domain;

public class Station {

    private Long id;
    private String name;

    public Station(final String name) {
        this.name = name;
    }

    public void updateStationNameOnAdd(final Station station) {
        name = station.name;
    }

    public boolean isSame(final Station other) {
        return name.equals(other.name);
    }
}
