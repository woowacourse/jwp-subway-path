package subway.line.domain;

import subway.line.exception.InvalidAdditionalFareException;

import java.util.Objects;

public class LineMetadata {

    private final LineName name;
    private final int additionalFare;

    public LineMetadata(String name, int additionalFare) {
        validateIsPositive(additionalFare);
        this.name = new LineName(name);
        this.additionalFare = additionalFare;
    }

    private void validateIsPositive(int additionalFare) {
        if (additionalFare < 0) {
            throw new InvalidAdditionalFareException("추가 요금은 음수가 될 수 없습니다. additionalFare: " + additionalFare);
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
        LineMetadata lineMetadata = (LineMetadata) o;
        return additionalFare == lineMetadata.additionalFare && Objects.equals(name, lineMetadata.name);
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
