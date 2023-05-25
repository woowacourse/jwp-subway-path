package subway.domain.policy.fare;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import subway.domain.Distance;
import subway.domain.Money;
import subway.domain.route.RouteFinder;
import subway.domain.station.Station;

class DistanceFarePolicyTest {

  private SubwayFarePolicy subwayFarePolicy;

  private RouteFinder routeFinder;

  @BeforeEach
  void setUp() {
    routeFinder = mock(RouteFinder.class);

    subwayFarePolicy = new DistanceFarePolicy();
  }

  @ParameterizedTest
  @MethodSource("calculatePriceFromDistance")
  @DisplayName("calculate() : 거리에 따라 요금을 계산할 수 있다.")
  void test_calculate(final Station departure, final Station arrival, final Money price,
      final Distance distance) throws Exception {
    //given
    when(routeFinder.findShortestRouteDistance(any(), any()))
        .thenReturn(distance);

    //when
    final Money result = subwayFarePolicy.calculate(routeFinder, departure, arrival);

    //then
    assertEquals(result, price);
  }

  static Stream<Arguments> calculatePriceFromDistance() {

    final Station start1 = new Station("A");
    final Station end1 = new Station("G");
    final Money money1 = new Money(1350);
    final Distance distance1 = new Distance(13);

    final Station start2 = new Station("A");
    final Station end2 = new Station("H");
    final Money money2 = new Money(1250);
    final Distance distance2 = new Distance(8);

    final Station start3 = new Station("G");
    final Station end3 = new Station("C");
    final Money money3 = new Money(1250);
    final Distance distance3 = new Distance(10);

    final Station start4 = new Station("F");
    final Station end4 = new Station("H");
    final Money money4 = new Money(1350);
    final Distance distance4 = new Distance(11);

    return Stream.of(
        Arguments.of(start1, end1, money1, distance1),
        Arguments.of(start2, end2, money2, distance2),
        Arguments.of(start3, end3, money3, distance3),
        Arguments.of(start4, end4, money4, distance4)
    );
  }
}
