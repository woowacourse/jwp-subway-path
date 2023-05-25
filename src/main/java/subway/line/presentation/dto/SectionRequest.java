package subway.line.presentation.dto;

public class SectionRequest {
    private final Long startingStationId;
    private final Long destinationStationId;
    private final int age;

    public SectionRequest(Long startingStationId, Long destinationStationId, int age) {
        this.startingStationId = startingStationId;
        this.destinationStationId = destinationStationId;
        this.age = age;
    }

    public Long getStartingStationId() {
        return startingStationId;
    }

    public Long getDestinationStationId() {
        return destinationStationId;
    }

    public int getAge() {
        return age;
    }
}