package subway.application.request;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreateLineRequest {

    @NotNull(message = "노선명은 null일 수 없습니다.")
    @Length(min = 1, max = 20, message = "노선명의 길이는 1 ~ 20이하이어야 합니다.")
    private String name;

    @NotNull
    @NotBlank(message = "노선 색상은 공백일 수 없습니다.")
    private String color;

    public CreateLineRequest() {
    }

    public CreateLineRequest(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
