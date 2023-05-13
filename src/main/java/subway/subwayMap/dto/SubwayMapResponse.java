package subway.subwayMap.dto;

import subway.station.domain.Station;

import java.util.List;

public class SubwayMapResponse {

    private Long id;
    private String name;
    private String color;
    private List<Station> stations;

    private SubwayMapResponse() {
    }

    public SubwayMapResponse(final Long id, final String name, final String color, final List<Station> stations) {
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
