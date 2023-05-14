package subway.dto;

import subway.domain.Line;
import subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class GetAllStationsInLineResponse {
    private final Long lineId;
    private final String lineName;
    private final List<StationDto> stations;

    public GetAllStationsInLineResponse(final Line line, final List<Station> stations) {
        this.lineId = line.getId();
        this.lineName = line.getName();
        this.stations = stations.stream()
                .map(station -> new StationDto(station.getId(), station.getName()))
                .collect(Collectors.toList());
    }

    public Long getLineId() {
        return lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public List<StationDto> getStations() {
        return stations;
    }
}