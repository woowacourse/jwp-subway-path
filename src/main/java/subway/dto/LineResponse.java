package subway.dto;

import subway.domain.Line;
import subway.domain.SectionMap;
import subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final List<String> stations;

    public LineResponse(final Long id, final String name, final String color) {
        this(id, name, color, List.of());
    }

    public LineResponse(final Long id, final String name, final String color, final List<String> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineResponse of(final Line line) {
        final List<String> stations = sectionMapToStations(line.getSectionMap());
        return new LineResponse(line.getId(), line.getName(), line.getColor(), stations);
    }

    private static List<String> sectionMapToStations(final SectionMap sectionMap) {
        return sectionMap.getAllStations().stream()
                .map(Station::getName)
                .collect(Collectors.toUnmodifiableList());
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

    public List<String> getStations() {
        return stations;
    }
}
