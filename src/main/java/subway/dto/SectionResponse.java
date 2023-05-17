package subway.dto;

import subway.domain.Line;
import subway.domain.Station;

public class SectionResponse {
    private final Long lineId;
    private final String lineName;
    private final Long sourceStationId;
    private final String sourceStationName;
    private final Long targetStationId;
    private final String targetStationName;

    public SectionResponse(
            Long lineId,
            String lineName,
            Long sourceStationId,
            String sourceStationName,
            Long targetStationId,
            String targetStationName
    ) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.sourceStationId = sourceStationId;
        this.sourceStationName = sourceStationName;
        this.targetStationId = targetStationId;
        this.targetStationName = targetStationName;
    }

    public static SectionResponse from(Line line, Station sourceStation, Station targetStation) {
        return new SectionResponse(
                line.getId(),
                line.getName(),
                sourceStation.getId(),
                sourceStation.getName(),
                targetStation.getId(),
                targetStation.getName()
        );
    }

    public Long getLineId() {
        return lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public String getSourceStationName() {
        return sourceStationName;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }

    public String getTargetStationName() {
        return targetStationName;
    }
}
