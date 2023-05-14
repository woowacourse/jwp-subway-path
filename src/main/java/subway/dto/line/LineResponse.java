package subway.dto.line;

import subway.entity.LineEntity;

public class LineResponse {

    private final Long lineId;
    private final long lineNumber;
    private final String name;
    private final String color;

    private LineResponse(final Long lineId, final long lineNumber, final String name, final String color) {
        this.lineId = lineId;
        this.lineNumber = lineNumber;
        this.name = name;
        this.color = color;
    }

    public static LineResponse from(final LineEntity lineEntity) {
        return new LineResponse(
                lineEntity.getLineId(),
                lineEntity.getLineNumber(),
                lineEntity.getName(),
                lineEntity.getColor()
        );
    }

    public Long getLineId() {
        return lineId;
    }

    public long getLineNumber() {
        return lineNumber;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
