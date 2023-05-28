package subway.dao.entity;

import subway.domain.fare.expense.LineExpense;
import subway.domain.vo.Distance;
import subway.domain.vo.Money;

import java.util.Objects;

public class LineExpenseEntity {

    private final Long id;
    private final String perExpense;
    private final Integer distance;
    private final Long lineId;

    public LineExpenseEntity(final String perExpense, final Integer distance, final Long lineId) {
        this(null, perExpense, distance, lineId);
    }

    public LineExpenseEntity(final Long id, final String perExpense, final Integer distance, final Long lineId) {
        this.id = id;
        this.perExpense = String.valueOf(perExpense);
        this.distance = distance;
        this.lineId = lineId;
    }

    public static LineExpenseEntity freeExpenseWithLineId(final Long lineId) {
        return new LineExpenseEntity("0", 0, lineId);
    }

    public LineExpense toDomain() {
        return new LineExpense(
                id, Money.from(perExpense),
                Distance.from(distance),
                lineId
        );
    }

    public Long getId() {
        return id;
    }

    public String getPerExpense() {
        return perExpense;
    }

    public Integer getPerDistance() {
        return distance;
    }

    public Long getLineId() {
        return lineId;
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final LineExpenseEntity that = (LineExpenseEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
