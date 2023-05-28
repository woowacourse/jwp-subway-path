package subway.fixture;

import subway.domain.fare.expense.LineExpensePolicy;

import static subway.fixture.LineExpenseFixture.노선_추가_비용;

public abstract class LineExpensePolicyFixture {

    public static LineExpensePolicy 노선_추가_비용_정책_생성_데이터(final Long 노선_추가_비용_정책_식별자값, final String 추가_비용, final int 거리당, final long 노선_식별자값) {
        return new LineExpensePolicy(노선_추가_비용(
                노선_추가_비용_정책_식별자값,
                추가_비용,
                거리당,
                노선_식별자값
        ));
    }
}
