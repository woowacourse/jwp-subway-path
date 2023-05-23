package subway.dto.request;

import javax.validation.constraints.NotEmpty;
import subway.service.dto.LineDto;

public class CreateLineRequest {

    @NotEmpty(message = "노선 이름이 입력되지 않았습니다.")
    private final String name;

    @NotEmpty(message = "노선 색상이 입력되지 않았습니다.")
    private final String color;

    public CreateLineRequest(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public LineDto toDto(){
        return new LineDto(name, color);
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
