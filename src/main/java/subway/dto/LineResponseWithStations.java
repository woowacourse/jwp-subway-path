package subway.dto;

import java.util.List;

import subway.domain.station.Station;

public class LineResponseWithStations {

    private final Long id;
    private final String name;
    private final String color;
    private final List<Station> stations;

    public LineResponseWithStations(final Long id, final String name, final String color, final List<Station> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Station> getStations() {
        return stations;
    }
}
