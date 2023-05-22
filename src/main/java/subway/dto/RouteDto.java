package subway.dto;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.subway.Route;

public class RouteDto {
    private final long lineId;
    private final String lineName;
    private final List<StationDto> stations;

    public RouteDto(long lineId, String lineName, List<StationDto> stations) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.stations = stations;
    }

    public static RouteDto from(Route route) {
        List<StationDto> stations = route.getStations().stream()
                .map(it -> new StationDto(it.getId(), it.getName()))
                .collect(Collectors.toList());
        return new RouteDto(route.getLineId(), route.getLineName(), stations);
    }

    public long getLineId() {
        return lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public List<StationDto> getStations() {
        return stations;
    }
}
