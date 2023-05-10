package subway.dto;

import subway.dao.entity.LineEntity;

public class LineResponse {
    private Long lineId;
    private String lineName;
    private String color;

    public LineResponse(Long lineId, String lineName, String color) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.color = color;
    }

    public static LineResponse from(LineEntity lineEntity) {
        return new LineResponse(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor());
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
