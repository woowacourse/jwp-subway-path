package subway.domain.fare.expense;

import subway.domain.Line;
import subway.domain.fare.FarePolicy;
import subway.domain.fare.FareInfo;
import subway.domain.vo.Distance;
import subway.domain.vo.Money;

import java.util.Map;

public class LineExpensePolicy implements FarePolicy {

    private final LineExpense lineExpense;

    public LineExpensePolicy(final LineExpense lineExpense) {
        this.lineExpense = lineExpense;
    }

    @Override
    public FareInfo doCalculate(final FareInfo fareInfo) {
        final Money additionalExpense = fareInfo.getLineDistance()
                .getLineTotalDistance()
                .entrySet()
                .stream()
                .map(this::calculateLineExpense)
                .reduce(Money::plus)
                .orElseThrow(() -> new IllegalStateException("노선별 거리당 추가 비용 계산 중 오류가 발생하였습니다."));

        return fareInfo.plusFare(additionalExpense);
    }

    private Money calculateLineExpense(final Map.Entry<Line, Distance> lineDistance) {
        if (lineExpense.isSameLineId(lineDistance.getKey().getId())) {
            return lineExpense.calculate(lineDistance.getValue());
        }

        return Money.ZERO;
    }
}
