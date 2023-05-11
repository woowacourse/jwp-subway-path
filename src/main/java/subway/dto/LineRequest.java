package subway.dto;

import javax.validation.constraints.NotEmpty;

public class LineRequest {

    @NotEmpty(message = "노선 명이 입력되지 않았습니다.")
    private final String name;

    @NotEmpty(message = "노선 색상이 입력되지 않았습니다.")
    private final String color;

    public LineRequest() {
        this(null, null);
    }

    public LineRequest(final String name, final String color) {
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
