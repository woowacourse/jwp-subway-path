package subway.ui.dto;

import subway.domain.Line;

public class PostLineResponse {

    private final Long lineId;
    private final String lineName;
    private final String lineColor;

    public PostLineResponse(Long lineId, String lineName, String lineColor) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.lineColor = lineColor;
    }

    public static PostLineResponse of(Line line) {
        return new PostLineResponse(line.getId(), line.getName(), line.getColor());
    }

    public Long getLineId() {
        return lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public String getLineColor() {
        return lineColor;
    }
}
