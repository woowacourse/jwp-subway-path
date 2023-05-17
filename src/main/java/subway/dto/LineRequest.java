package subway.dto;

public class LineRequest {
    private final String name;
    private final String color;

    private LineRequest() {
        this.name = null;
        this.color = null;
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
