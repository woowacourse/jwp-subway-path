package subway.dto;

import subway.domain.Line;

public class LineResponse {
    private final Long id;
    private final String name;

    public LineResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    private LineResponse() {
        this(null, null);
    }

    public static LineResponse of(final Line line) {
        return new LineResponse(line.getId(), line.getName().getValue());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
