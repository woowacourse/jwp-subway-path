package subway.dao.line;

import java.util.Objects;

public class LineEntity {
    private final Long lineId;
    private final String name;
    private final String color;
    private final int extraFare;

    public LineEntity(final String name, final String color) {
        this(null, name, color, 0);
    }

    public LineEntity(final Long lineId, final String name, final String color) {
        this(lineId, name, color, 0);
    }

    public LineEntity(final Long lineId, final String name, final String color, final int extraFare) {
        this.lineId = lineId;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
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

    public int getExtraFare() {
        return extraFare;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LineEntity that = (LineEntity) o;
        return Objects.equals(getLineId(), that.getLineId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLineId());
    }
}
