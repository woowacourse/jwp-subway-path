package subway.service.dto;

import subway.domain.line.Line;
import subway.domain.station.Station;

import java.util.List;
import java.util.stream.Collectors;

public class LineDto {

    private final Long id;
    private final String name;
    private final String color;
    private final List<StationDto> stations;

    public LineDto(final Long id, final String name, final String color, final List<StationDto> stationDtos) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stationDtos;
    }

    public static LineDto of(final Line line, final List<Station> stations) {
        final List<StationDto> stationDtos = stations.stream()
                .map(StationDto::of)
                .collect(Collectors.toUnmodifiableList());
        return new LineDto(line.getId(), line.getName(), line.getColor(), stationDtos);
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

    public List<StationDto> getStations() {
        return stations;
    }
}
