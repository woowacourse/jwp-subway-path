package subway.application.dto;

import java.util.List;

public class LineDto {
    private final Long id;
    private final String name;
    private final List<StationDto> stations;

    public LineDto(Long id, String name, List<StationDto> stations) {
        this.id = id;
        this.name = name;
        this.stations = stations;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StationDto> getStations() {
        return stations;
    }
}
