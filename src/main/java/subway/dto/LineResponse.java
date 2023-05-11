package subway.dto;

import subway.domain.Line;

public class LineResponse {
    private final Long id;
    private final String name;

    public LineResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static LineResponse of(final Line line) {
        return new LineResponse(line.getId(), line.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
