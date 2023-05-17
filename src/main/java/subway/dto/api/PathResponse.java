package subway.dto.api;

import java.util.List;
import subway.dto.domain.LineDto;
import subway.dto.domain.StationDto;

public class PathResponse {
    private final LineDto line;
    private final List<StationDto> stations;

    public PathResponse(LineDto line, List<StationDto> stations) {
        this.line = line;
        this.stations = stations;
    }

    public LineDto getLine() {
        return line;
    }

    public List<StationDto> getStations() {
        return stations;
    }
}