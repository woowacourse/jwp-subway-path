package subway.domain.line.dto;

public class LineRequest {

    private String name;
    private String color;

    private LineRequest() {
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
