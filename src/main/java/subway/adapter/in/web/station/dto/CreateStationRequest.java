package subway.adapter.in.web.station.dto;

import javax.validation.constraints.NotNull;
import subway.application.port.in.station.dto.command.CreateStationCommand;

public class CreateStationRequest {

    @NotNull(message = "이름 정보가 없습니다.")
    private String name;

    private CreateStationRequest() {
    }

    public CreateStationRequest(String name) {
        this.name = name;
    }

    public CreateStationCommand toCommand() {
        return new CreateStationCommand(name);
    }

    public String getName() {
        return name;
    }
}
