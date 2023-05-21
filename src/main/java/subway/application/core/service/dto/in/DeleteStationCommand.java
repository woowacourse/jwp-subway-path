package subway.application.core.service.dto.in;

import javax.validation.constraints.NotNull;

public class DeleteStationCommand {

    @NotNull
    private final Long lineId;
    @NotNull
    private final Long stationId;

    public DeleteStationCommand(Long lineId, Long stationId) {
        this.lineId = lineId;
        this.stationId = stationId;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getStationId() {
        return stationId;
    }
}
