package subway.application.service.command.in;

import subway.application.service.command.SelfValidating;

import javax.validation.constraints.NotNull;

public class DeleteStationCommand extends SelfValidating<DeleteStationCommand> {

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
