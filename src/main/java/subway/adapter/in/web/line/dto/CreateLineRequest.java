package subway.adapter.in.web.line.dto;

import javax.validation.constraints.NotNull;
import subway.application.port.in.line.dto.command.CreateLineCommand;

public class CreateLineRequest {

    @NotNull(message = "이름 정보가 없습니다.")
    private String name;

    @NotNull(message = "색상 정보가 없습니다.")
    private String color;

    public CreateLineRequest() {
    }

    public CreateLineRequest(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public CreateLineCommand toCommand() {
        return new CreateLineCommand(name, color);
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
