package subway.ui.dto.response;

import subway.domain.Line;

public class LineResponse {

    private Long id;
    private String name;

    private LineResponse() {
    }

    private LineResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static LineResponse from(final Line line) {
        return new LineResponse(line.getId(), line.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
