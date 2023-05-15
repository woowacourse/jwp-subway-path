package subway.controller.dto;

public class LineRequest {

    private String name;

    private LineRequest() {
    }

    public LineRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
