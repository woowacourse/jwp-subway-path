package subway.service.dto;

public class StationDto {

    private final Long lineId;
    private final String name;

    public StationDto(final Long lineId, final String name) {
        this.lineId = lineId;
        this.name = name;
    }

    public Long getLineId() {
        return lineId;
    }

    public String getName() {
        return name;
    }
}
