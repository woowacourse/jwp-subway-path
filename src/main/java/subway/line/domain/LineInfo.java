package subway.line.domain;

import subway.line.exception.InvalidAdditionalFareException;

import java.util.Objects;

public class LineInfo {

    private final LineName name;
    private final int additionalFare;

    public LineInfo(String name, int additionalFare) {
        validateIsPositive(additionalFare);
        this.name = new LineName(name);
        this.additionalFare = additionalFare;
    }

    private void validateIsPositive(int additionalFare) {
        if (additionalFare < 0) {
            throw new InvalidAdditionalFareException(additionalFare);
        }
    }

    public String getName() {
        return name.getName();
    }

    public int getAdditionalFare() {
        return additionalFare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineInfo lineInfo = (LineInfo) o;
        return additionalFare == lineInfo.additionalFare && Objects.equals(name, lineInfo.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, additionalFare);
    }

    @Override
    public String toString() {
        return "{name=" + name +
                ", additionalFare=" + additionalFare +
                '}';
    }
}
