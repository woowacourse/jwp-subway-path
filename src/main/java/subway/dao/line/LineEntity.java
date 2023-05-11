package subway.dao.line;

public class LineEntity {
    private final Long lineId;
    private final String name;
    private final String color;

    public LineEntity(final String name, final String color) {
        this(null, name, color);
    }

    public LineEntity(final Long lineId, final String name, final String color) {
        this.lineId = lineId;
        this.name = name;
        this.color = color;
    }

    public Long getLineId() {
        return lineId;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
