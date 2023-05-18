package subway.adapter.in.web.route.dto;

import javax.validation.constraints.NotNull;
import subway.application.port.in.route.dto.command.FindRouteCommand;

public class FindRouteRequest {

    @NotNull(message = "출발역 id가 없습니다.")
    private Long sourceStationId;

    @NotNull(message = "도착역 id가 없습니다.")
    private Long targetStationId;

    public FindRouteRequest() {
    }

    public FindRouteRequest(final Long sourceStationId, final Long targetStationId) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public FindRouteCommand toCommand() {
        return new FindRouteCommand(sourceStationId, targetStationId);
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }
}
