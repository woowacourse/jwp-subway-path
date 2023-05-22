package subway.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.line.Line;
import subway.dto.StationDto;

public class GetAllStationsInLineResponse {
    private final long lineId;
    private final String lineName;
    private final List<StationDto> stations;

    private GetAllStationsInLineResponse(long lineId, String lineName, List<StationDto> stations) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.stations = stations;
    }

    public static GetAllStationsInLineResponse from(Line line) {
        List<StationDto> stations = line.getStations().stream()
                .map(station -> new StationDto(station.getId(), station.getName()))
                .collect(Collectors.toList());
        return new GetAllStationsInLineResponse(line.getId(), line.getName(), stations);
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
