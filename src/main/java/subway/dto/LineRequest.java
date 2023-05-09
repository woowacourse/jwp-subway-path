package subway.dto;

public class LineRequest {
    private final String name;

    public LineRequest(final String name) {
        this.name = name;
    }

    private LineRequest() {
        this(null);
    }

    public String getName() {
        return name;
    }
}
