package subway.dto;

public class LineRequest {
    private String lineName;

    public LineRequest() {
    }

    public LineRequest(String lineName) {
        this.lineName = lineName;
    }

    public String getLineName() {
        return lineName;
    }

}
