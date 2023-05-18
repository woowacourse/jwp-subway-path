package subway.ui.dto.request;

public class LineCreateRequest {
    private String name;

    public LineCreateRequest() {
    }

    public LineCreateRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
