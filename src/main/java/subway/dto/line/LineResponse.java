package subway.dto.line;

import subway.dao.entity.LineEntity;

public class LineResponse {
    private Long lineId;
    private String lineName;
    private String color;
    private long extraFee;

    public LineResponse(Long lineId, String lineName, String color, long extraFee) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.color = color;
        this.extraFee = extraFee;
    }

    public static LineResponse from(LineEntity lineEntity) {
        return new LineResponse(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), lineEntity.getExtraFee());
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

    public long getExtraFee() {
        return extraFee;
    }
}
