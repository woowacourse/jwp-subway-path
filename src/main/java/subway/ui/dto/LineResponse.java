package subway.ui.dto;

import subway.domain.Line;

public class LineResponse {

    private Long id;
    private String name;

    private LineResponse() {
    }

    public LineResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static LineResponse of(Line line) {
        return new LineResponse(line.getId(), line.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
