package subway.policy.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import subway.line.application.LineQueryService;
import subway.line.domain.Line;
import subway.line.domain.Station;
import subway.policy.infrastructure.AgeDiscountPolicy;
import subway.policy.infrastructure.DiscountCondition;
import subway.policy.infrastructure.DistanceFarePolicy;
import subway.policy.infrastructure.LineFarePolicy;
import subway.value_object.Money;

@SpringBootTest
class ChargePolicyCompositeTest {

  @Autowired
  private ChargePolicyComposite pricePolicyComposite;

  @Autowired
  private LineQueryService lineQueryService;

  @BeforeEach
  void setUp() {
    pricePolicyComposite = new ChargePolicyComposite(
        List.of(new DistanceFarePolicy(),
            new LineFarePolicy()),
        List.of(new AgeDiscountPolicy())
    );
  }

  /**
   * 11    5 F  -  G  - H 4  |          |  2 |          | A  -  B  -  C  - D 1     2     3
   */
  @Test
  @DisplayName("calculate() : 모든 추가 요금 정책을 계산할 수 있다.")
  void test_calculate() throws Exception {
    //given
    final Station departure = new Station("A");
    final Station arrival = new Station("G");
    final List<Line> lines = lineQueryService.searchAllLine();

    //when
    final Money totalMoney = pricePolicyComposite.calculate(lines, departure, arrival);

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
}
