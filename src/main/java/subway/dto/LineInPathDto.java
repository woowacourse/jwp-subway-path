package subway.dto;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.LineInPath;

public class LineInPathDto {
    private final Long lineId;
    private final String lineName;
    private final List<StationDto> stations;

    public LineInPathDto(Long lineId, String lineName, List<StationDto> stations) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.stations = stations;
    }

    public static LineInPathDto from(LineInPath lineInPath) {
        List<StationDto> stations = lineInPath.getStations().stream()
                .map(it -> new StationDto(it.getId(), it.getName()))
                .collect(Collectors.toList());
        return new LineInPathDto(lineInPath.getLineId(), lineInPath.getLineName(), stations);
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
