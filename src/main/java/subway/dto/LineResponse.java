package subway.dto;

import subway.domain.Line;

public class LineResponse {
    private Long id;

    public LineResponse(Long id) {
        this.id = id;
    }

    public LineResponse() {
    }

    public static LineResponse of(Line line) {
        return new LineResponse(line.getId());
    }

    public Long getId() {
        return id;
    }
}
