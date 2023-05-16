package subway.dto.line;

import subway.domain.Line;

public class LineResponse {
    private Long lineId;
    private String lineName;
    private String color;

    public LineResponse(Long lineId, String lineName, String color) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.color = color;
    }

    public static LineResponse from(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor());
    }

    public Long getLineId() {
        return lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public String getColor() {
        return color;
    }
}
