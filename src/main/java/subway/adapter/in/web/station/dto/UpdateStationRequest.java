package subway.adapter.in.web.station.dto;

import javax.validation.constraints.NotNull;
import subway.application.port.in.station.dto.command.UpdateStationCommand;

public class UpdateStationRequest {

    @NotNull(message = "이름 정보가 없습니다.")
    private String name;

    private UpdateStationRequest() {
    }

    public UpdateStationRequest(String name) {
        this.name = name;
    }

    public UpdateStationCommand toCommand(final long stationId) {
        return new UpdateStationCommand(stationId, name);
    }

    public String getName() {
        return name;
    }
}
