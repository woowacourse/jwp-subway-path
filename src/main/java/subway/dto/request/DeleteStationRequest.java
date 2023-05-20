package subway.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import subway.dto.StationDto;

public class DeleteStationRequest {

    @NotNull(message = "노선이 입력되지 않았습니다.")
    private final Long lineId;

    @NotEmpty(message = "역 이름이 입력되지 않았습니다.")
    private final String station;

    public DeleteStationRequest(final Long lineId, final String station) {
        this.lineId = lineId;
        this.station = station;
    }

    public StationDto toDto() {
        return new StationDto(lineId, station);
    }

    public Long getLineId() {
        return lineId;
    }

    public String getStation() {
        return station;
    }
}
