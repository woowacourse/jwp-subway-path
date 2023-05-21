package subway.domain.line.domain;

import subway.domain.line.entity.StationEntity;

import java.util.List;

public class Line {

    private final Long id;
    private final String name;
    private final String color;
    private final List<StationEntity> stations;

    public Line(final Long id, final String name, final String color, final List<StationEntity> stations) {
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

    public List<StationEntity> getStations() {
        return stations;
    }
}
