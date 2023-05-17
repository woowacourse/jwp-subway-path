package subway.domain.lineDetail.dto;

public class LineDetailRequest {

    private String name;
    private String color;

    private LineDetailRequest() {
    }

    public LineDetailRequest(final String name, final String color) {
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
