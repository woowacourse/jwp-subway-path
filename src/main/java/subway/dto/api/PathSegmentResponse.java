package subway.dto.api;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.domain.LineDto;
import subway.dto.domain.StationDto;

public class PathSegmentResponse {
    private final LineDto line;
    private final List<StationDto> stations;

    public PathSegmentResponse(LineDto line, List<StationDto> stations) {
        this.line = line;
        this.stations = stations;
    }

    public static PathSegmentResponse of(Line line, List<Station> stations) {
        List<StationDto> stationDtos = stations.stream().map(StationDto::from).collect(Collectors.toList());
        return new PathSegmentResponse(LineDto.from(line), stationDtos);
    }

    public LineDto getLine() {
        return line;
    }

    public List<StationDto> getStations() {
        return stations;
    }
}