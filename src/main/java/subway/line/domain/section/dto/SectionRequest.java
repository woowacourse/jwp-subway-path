package subway.line.domain.section.dto;

public class SectionRequest {
    private final Long startingStationId;
    private final Long destinationStationId;

    public SectionRequest(Long startingStationId, Long destinationStationId) {
        this.startingStationId = startingStationId;
        this.destinationStationId = destinationStationId;
    }

    public Long getStartingStationId() {
        return startingStationId;
    }

    public Long getDestinationStationId() {
        return destinationStationId;
    }
}