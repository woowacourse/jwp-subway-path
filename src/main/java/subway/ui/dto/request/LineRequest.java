package subway.ui.dto.request;

public class LineRequest {

    private String name;

    private LineRequest() {
    }

    public LineRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
