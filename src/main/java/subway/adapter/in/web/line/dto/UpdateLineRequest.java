package subway.adapter.in.web.line.dto;

import javax.validation.constraints.NotNull;
import subway.application.port.in.line.dto.command.UpdateLineInfoCommand;

public class UpdateLineRequest {

    @NotNull(message = "이름 정보가 없습니다.")
    private String name;

    @NotNull(message = "색상 정보가 없습니다.")
    private String color;

    private UpdateLineRequest() {
    }

    public UpdateLineRequest(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public UpdateLineInfoCommand toCommand(final long lineId) {
        return new UpdateLineInfoCommand(lineId, getName(), getColor());
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
