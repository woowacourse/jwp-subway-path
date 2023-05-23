package subway.domain.fare.expense;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.fare.FareInfo;
import subway.domain.route.Route;
import subway.domain.route.RouteEdge;
import subway.domain.vo.Distance;
import subway.domain.vo.Money;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import static subway.domain.PassengerType.ADULT;
import static subway.fixture.DistanceFixture.거리;
import static subway.fixture.LineFixture.노선;
import static subway.fixture.MoneyFixture.금액;
import static subway.fixture.SectionFixture.구간;
import static subway.fixture.StationFixture.역;

@DisplayNameGeneration(ReplaceUnderscores.class)
class LineExpensePolicyTest {

    @Test
    void 노선_추가_비용_정책으로_인해_10km_이용시_추가_요금이_1000원씩_증가한다() {
        // given
        final Section 구간AB = 구간(거리(10), 역("A"), 역("B"));
        final Section 구간BC = 구간(거리(10), 역("B"), 역("C"));
        final Section 구간CD = 구간(거리(10), 역("C"), 역("D"));
        final Section 구간DE = 구간(거리(10), 역("D"), 역("E"));

        final Line 파랑_노선 = 노선(1L, "1", "파랑", List.of(구간AB, 구간BC));
        final Line 초록_노선 = 노선(2L, "2", "초록", List.of(구간CD, 구간DE));

        final Route 최단_경로 = new Route(
                역("A"),
                역("E"),
                List.of(역("C")),
                List.of(RouteEdge.from(구간AB, 파랑_노선),
                        RouteEdge.from(구간BC, 파랑_노선),
                        RouteEdge.from(구간CD, 초록_노선),
                        RouteEdge.from(구간DE, 초록_노선)
                ), 거리(40)
        );

        final LineExpensePolicy 파랑_노선_추가_비용_정책
                = new LineExpensePolicy(new LineExpense(Money.from("1000"), Distance.from(10), 1L));

        // when
        final FareInfo 기존_금액 = FareInfo.from(ADULT, 최단_경로);
        final FareInfo 계산된_금액 = 파랑_노선_추가_비용_정책.doCalculate(기존_금액);
        final Money 총금액 = 계산된_금액.getTotalFare();

        // then
        assertThat(총금액).isEqualTo( 금액("2000"));
    }
}
