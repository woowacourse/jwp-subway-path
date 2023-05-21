package subway.dto.line;

import subway.entity.LineEntity;

public class LineResponse {
    private Long id;

    private LineResponse() {
    }

    public LineResponse(Long id) {
        this.id = id;
    }

    public static LineResponse from(LineEntity line) {
        return new LineResponse(line.getId());
    }

    public Long getId() {
        return id;
    }
}
