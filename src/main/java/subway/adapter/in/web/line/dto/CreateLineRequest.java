package subway.adapter.in.web.line.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import subway.application.port.in.line.dto.command.CreateLineCommand;

public class CreateLineRequest {

    @NotNull(message = "이름 정보가 없습니다.")
    private String name;

    @NotNull(message = "색상 정보가 없습니다.")
    private String color;

    @PositiveOrZero(message = "추가 요금은 0원 이상이어야합니다.")
    private Integer surcharge;

    private CreateLineRequest() {
    }

    public CreateLineRequest(final String name, final String color, final Integer surcharge) {
        this.name = name;
        this.color = color;
        this.surcharge = surcharge;
    }

    public CreateLineCommand toCommand() {
        return new CreateLineCommand(name, color, surcharge);
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Integer getSurcharge() {
        return surcharge;
    }
}
