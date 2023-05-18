package subway.dao.line;

import java.util.Objects;

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
