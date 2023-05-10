package subway.dto;

public class LineCreateRequest {
    private String lineName;
    private String color;

    public LineCreateRequest() {
    }

    public LineCreateRequest(String lineName, String color) {
        this.lineName = lineName;
        this.color = color;
    }

    public String getLineName() {
        return lineName;
    }

    public String getColor() {
        return color;
    }
}
