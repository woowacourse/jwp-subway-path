package subway.domain;

public class Station {

    private Long id;
    private String name;

    public Station(final String name) {
        this.name = name;
    }

    public void updateStationName(final Station station) {
        name = station.name;
    }

    public boolean isSame(final Station other) {
        return name.equals(other.name);
    }

    public String getName() {
        return name;
    }
}
