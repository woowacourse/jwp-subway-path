package subway.entity;

import java.util.Objects;

public class LineEntity {

    private final Long lineId;
    private final Long lineNumber;
    private final String name;
    private final String color;
    private final int additionalFare;

    public LineEntity(Long lineNumber, String name, String color) {
        this(null, lineNumber, name, color, 0);
    }

    public LineEntity(Long lineNumber, String name, String color, int additionalFare) {
        this(null, lineNumber, name, color, additionalFare);
    }

    public LineEntity(Long lineId, Long lineNumber, String name, String color, int additionalFare) {
        this.lineId = lineId;
        this.lineNumber = lineNumber;
        this.name = name;
        this.color = color;
        this.additionalFare = additionalFare;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getLineNumber() {
        return lineNumber;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getAdditionalFare() {
        return additionalFare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LineEntity that = (LineEntity) o;
        return additionalFare == that.additionalFare && Objects.equals(lineId, that.lineId) && Objects.equals(lineNumber, that.lineNumber) && Objects.equals(name, that.name)
                && Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineId, lineNumber, name, color, additionalFare);
    }
}
