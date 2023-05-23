package subway.application.response;

import subway.domain.fare.expense.LineExpense;

public class LineExpenseResponse {

    private Long id;
    private String perExpense;
    private Integer perDistance;
    private Long lineId;

    public LineExpenseResponse() {
    }

    public LineExpenseResponse(final Long id, final String perExpense, final Integer perDistance, final Long lineId) {
        this.id = id;
        this.perExpense = perExpense;
        this.perDistance = perDistance;
        this.lineId = lineId;
    }

    public static LineExpenseResponse from(final LineExpense LineExpense) {
        return new LineExpenseResponse(
                LineExpense.getId(),
                LineExpense.getPerFare().getValue(),
                LineExpense.getPerDistance().getValue(),
                LineExpense.getLineId()
        );
    }

    public Long getId() {
        return id;
    }

    public String getPerExpense() {
        return perExpense;
    }

    public Integer getPerDistance() {
        return perDistance;
    }

    public Long getLineId() {
        return lineId;
    }
}
