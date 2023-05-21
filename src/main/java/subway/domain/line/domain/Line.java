package subway.domain.line.domain;

import subway.domain.line.entity.SectionEntity;

import java.util.List;

public class Line {

    private final String name;
    private final String color;
    private final List<SectionEntity> stations;

    public Line(final String name, final String color, final List<SectionEntity> stations) {
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<SectionEntity> getStations() {
        return stations;
    }
}
