package subway.dto.line;

import subway.entity.LineEntity;

public class LineCreateResponse {
    private Long id;

    private LineCreateResponse() {
    }

    public LineCreateResponse(Long id) {
        this.id = id;
    }

    public static LineCreateResponse from(LineEntity line) {
        return new LineCreateResponse(line.getId());
    }

    public Long getId() {
        return id;
    }
}
