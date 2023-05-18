package subway.dto.request;

import org.jetbrains.annotations.NotNull;

public class LineRequest {

    @NotNull("이름은 비어있을 수 없습니다.")
    private String name;
    @NotNull("색상은 비어있을 수 없습니다.")
    private String color;

    public LineRequest() {
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
