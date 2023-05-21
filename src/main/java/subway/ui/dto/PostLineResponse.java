package subway.ui.dto;

import subway.domain.Line;

public class PostLineResponse {

    private final Long lineId;
    private final String lineName;
    private final String lineColor;
    private final Integer additionalCharge;

    public PostLineResponse(Long lineId, String lineName, String lineColor, Integer additionalCharge) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.lineColor = lineColor;
        this.additionalCharge = additionalCharge;
    }

    public static PostLineResponse of(Line line) {
        return new PostLineResponse(line.getId(), line.getName(), line.getColor(), line.getAdditionalCharge());
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

    public Integer getAdditionalCharge() {
        return additionalCharge;
    }
}
