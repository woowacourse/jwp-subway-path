package subway.dto;

import subway.entity.LineEntity;

public class LineResponse {
    private Long id;

    public LineResponse(Long id) {
        this.id = id;
    }

    public LineResponse() {
    }

    public static LineResponse of(LineEntity line) {
        return new LineResponse(line.getId());
    }

    public Long getId() {
        return id;
    }
}
