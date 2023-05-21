package subway.domain.policy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.Money;
import subway.domain.line.Line;
import subway.domain.policy.discount.AgeDiscountPolicy;
import subway.domain.policy.discount.DiscountCondition;
import subway.domain.policy.fare.DefaultFarePolicy;
import subway.domain.policy.fare.LineFarePolicy;
import subway.domain.route.Route;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.domain.station.Stations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChargePolicyCompositeTest {

    private ChargePolicyComposite pricePolicyComposite;

    @BeforeEach
    void setUp() {
        pricePolicyComposite = new ChargePolicyComposite(
                List.of(new DefaultFarePolicy(),
                        new LineFarePolicy()),
                List.of(new AgeDiscountPolicy())
        );
    }

    /**
     * 11   5
     * F -- G -- H
     * 4 |         | 4
     * |         |
     * A -- B -- C -- D
     * 1   2    3
     */
    @Test
    @DisplayName("calculate() : 모든 추가 요금 정책을 계산할 수 있다.")
    void test_calculate() throws Exception {
        //given
        final List<Line> lines = createDefaultLines();
        final Route route = new Route(
                lines,
                new Station("A"),
                new Station("G")
        );

        //when
        final Money totalMoney = pricePolicyComposite.calculate(route);

        //then
        assertEquals(new Money(2350), totalMoney);
    }

    @Test
    @DisplayName("discount() : 모든 할인 요금 정책을 계산할 수 있다.")
    void test_discount() throws Exception {
        //given
        final DiscountCondition discountCondition = new DiscountCondition(14);
        final Money money = new Money(10350);

        //when
        final Money discountedMoney = pricePolicyComposite.discount(discountCondition, money);

        //then
        assertEquals(new Money(8000), discountedMoney);
    }

    private List<Line> createDefaultLines() {
        final Stations stations1 = new Stations(new Station("A"), new Station("B"), 1);
        final Stations stations2 = new Stations(new Station("B"), new Station("C"), 2);
        final Stations stations3 = new Stations(new Station("C"), new Station("D"), 3);

        final Section section1 = new Section(stations1);
        final Section section2 = new Section(stations2);
        final Section section3 = new Section(stations3);

        final Line line1 = new Line(1L, "1호선", List.of(section1, section2, section3));

        final Stations stations4 = new Stations(new Station("B"), new Station("F"), 4);
        final Stations stations5 = new Stations(new Station("F"), new Station("G"), 11);
        final Stations stations6 = new Stations(new Station("G"), new Station("H"), 5);
        final Stations stations7 = new Stations(new Station("H"), new Station("D"), 4);

        final Section section4 = new Section(stations4);
        final Section section5 = new Section(stations5);
        final Section section6 = new Section(stations6);
        final Section section7 = new Section(stations7);

        final Line line2 = new Line(2L, "2호선",
                                    List.of(section4, section5, section6, section7));

        return List.of(line1, line2);
    }
}
