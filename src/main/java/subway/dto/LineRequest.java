package subway.dto;

import javax.validation.constraints.NotBlank;

public class LineRequest {
    @NotBlank(message = "노선 이름이 필요합니다.")
    private String name;
    @NotBlank(message = "노선 색상이 필요합니다.")
    private String color;
    private int additionalFare;

    public LineRequest() {
    }

    public LineRequest(final String name, final String color, final int additionalFare) {
        this.name = name;
        this.color = color;
        this.additionalFare = additionalFare;
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

    public int getAdditionalFare() {
        return additionalFare;
    }
}
