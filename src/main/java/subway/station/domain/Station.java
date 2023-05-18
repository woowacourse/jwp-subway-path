package subway.station.domain;

import subway.vo.Name;

import java.util.Map;

public class Station {

    private final Long id;
    private Name name;

    private Station(final Long id, final Name name) {
        this.id = id;
        this.name = name;
    }

    public static Station from(final String name) {
        return new Station(null, Name.from(name));
    }

    public static Station from(final Long id) {
        return new Station(id, null);
    }

    public static Station of(final Long id, final Name name) {
        return new Station(id, name);
    }

    public boolean isFinalStations(final Map<String, Station> finalStations) {
        return finalStations.values().stream()
                .anyMatch(station -> station.getName() == name);
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public String getNameValue() {
        return name.getValue();
    }

    public void updateInfo(final Name name) {
        this.name = name;
    }

}
