package subway.dto;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Station;

public class GetAllStationsInLineResponse {
    private final long lineId;
    private final String lineName;
    private final List<StationDto> stations;

    public GetAllStationsInLineResponse(long lineId, String lineName, List<Station> stations) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.stations = stations.stream()
                .map(station -> new StationDto(station.getId(), station.getName()))
                .collect(Collectors.toList());
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
