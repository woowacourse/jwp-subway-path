package subway.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.StationDto;

public class GetAllStationsInLineResponse {
    private final long lineId;
    private final String lineName;
    private final List<StationDto> stations;

    private GetAllStationsInLineResponse(long lineId, String lineName, List<Station> stations) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.stations = stations.stream()
                .map(station -> new StationDto(station.getId(), station.getName()))
                .collect(Collectors.toList());
    }

    public static GetAllStationsInLineResponse fromDomain(Line line) {
        return new GetAllStationsInLineResponse(line.getId(), line.getName(), line.getStations());
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
