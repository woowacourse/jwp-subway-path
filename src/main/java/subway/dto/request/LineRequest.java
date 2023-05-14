package subway.dto.request;

import javax.validation.constraints.NotBlank;

public class LineRequest {

    @NotBlank(message = "[ERROR] 노선의 이름을 입력해야 합니다.")
    private String name;
    @NotBlank(message = "[ERROR] 노선의 색상을 입력해야 합니다.")
    private String color;

    private LineRequest() {
    }

    public LineRequest(String name, String color) {
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
