package subway.business.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.stream.Collectors;
import subway.business.domain.line.Line;
import subway.business.domain.line.Stations;

public class LinePathDto {
    @Schema(description = "노선 ID")
    private final Long lineId;

    @Schema(description = "노선 이름")
    private final String lineName;

    @Schema(description = "해당 노선에 대한 경로")
    private final List<StationDto> linePath;

    private LinePathDto(Long lineId, String lineName, List<StationDto> linePath) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.linePath = linePath;
    }

    public static LinePathDto of(Line line, Stations stations) {
        return new LinePathDto(
                line.getId(),
                line.getName(),
                stations.getStations().stream()
                        .map(StationDto::from)
                        .collect(Collectors.toList())
        );
    }
    
    public Long getLineId() {
        return lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public List<StationDto> getLinePath() {
        return linePath;
    }
}
