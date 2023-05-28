package subway.application.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class UpdateLineExpenseRequest {

    @NotNull(message = "추가 비용은 null일 수 없습니다.")
    private String perExpense;

    @NotNull(message = "추가 비용에 필요한 거리는 null일 수 없습니다.")
    @PositiveOrZero(message = "추가 비용에 필요한 거리는 0미만일 수 없습니다.")
    private Integer perDistance;

    public UpdateLineExpenseRequest() {
    }

    public UpdateLineExpenseRequest(final String perExpense, final Integer perDistance) {
        this.perExpense = perExpense;
        this.perDistance = perDistance;
    }

    public String getPerExpense() {
        return perExpense;
    }

    public Integer getPerDistance() {
        return perDistance;
    }
}
