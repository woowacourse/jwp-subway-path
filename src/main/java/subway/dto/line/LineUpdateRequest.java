package subway.dto.line;

public class LineUpdateRequest {
    private String lineName;
    private String color;

    public LineUpdateRequest(String lineName, String color) {
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
