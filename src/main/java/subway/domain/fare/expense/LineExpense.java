package subway.domain.fare.expense;

import subway.domain.vo.Distance;
import subway.domain.vo.Money;

import java.math.BigDecimal;
import java.util.Objects;

public class LineExpense {

    private final Long id;
    private final Money perFare;
    private final Distance perDistance;
    private final Long lineId;

    public LineExpense(final Money perFare, final Distance perDistance, final Long lineId) {
        this(null, perFare, perDistance, lineId);
    }

    public LineExpense(final Long id, final Money perFare, final Distance perDistance, final Long lineId) {
        this.id = id;
        this.perFare = perFare;
        this.perDistance = perDistance;
        this.lineId = lineId;
    }

    public Money calculate(final Distance distance) {
        if (perDistance.isZero()) {
            return Money.ZERO;
        }

        final Distance quotient = distance.quotient(perDistance);

        return perFare.multiply(BigDecimal.valueOf(quotient.getValue()));
    }

    public boolean isSameLineId(final Long otherLineId) {
        return Objects.equals(lineId, otherLineId);
    }

    public Long getId() {
        return id;
    }

    public Money getPerFare() {
        return perFare;
    }

    public Distance getPerDistance() {
        return perDistance;
    }

    public Long getLineId() {
        return lineId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final LineExpense that = (LineExpense) o;
        return Objects.equals(perFare, that.perFare) && Objects.equals(perDistance, that.perDistance) && Objects.equals(lineId, that.lineId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(perFare, perDistance, lineId);
    }

    @Override
    public String toString() {
        return "LineExpense{" +
                "id=" + id +
                ", perFare=" + perFare +
                ", perDistance=" + perDistance +
                ", lineId=" + lineId +
                '}';
    }
}
