package subway.domain.policy.fare;

import static subway.domain.Distance.DEFAULT_DISTANCE;
import static subway.domain.Distance.MID_DISTANCE;

import java.math.BigDecimal;
import subway.domain.Distance;
import subway.domain.Money;
import subway.domain.route.RouteFinder;

public class DistanceFarePolicy implements SubwayFarePolicy {

  private static final Money DEFAULT_PRICE = new Money(1250);
  private static final BigDecimal ADDITIONAL_FEE = BigDecimal.valueOf(100);
  private static final int MID_DISTANCE_RATE = 5;
  private static final int LONG_DISTANCE_RATE = 8;

  @Override
  public Money calculate(final RouteFinder routeFinder, final String departure,
      final String arrival) {
    final Distance distance = routeFinder.findShortestRouteDistance(departure, arrival);

    if (distance.isDefaultDistance()) {
      return DEFAULT_PRICE;
    }

    if (distance.isLongDistance()) {
      return DEFAULT_PRICE.add(calculateMidDistance(MID_DISTANCE))
          .add(calculateLongDistance(distance.minus(MID_DISTANCE)
              .minus(DEFAULT_DISTANCE)));
    }

    return DEFAULT_PRICE.add(calculateMidDistance(distance.minus(DEFAULT_DISTANCE)));
  }

  private BigDecimal calculateMidDistance(final Distance distance) {
    return calculatePriceBracket(distance, MID_DISTANCE_RATE);
  }

  private BigDecimal calculateLongDistance(final Distance distance) {
    return calculatePriceBracket(distance, LONG_DISTANCE_RATE);
  }

  private BigDecimal calculatePriceBracket(final Distance distance, final int rate) {
    return BigDecimal.valueOf(distance.calculateDistanceUnit(rate).getValue())
        .multiply(ADDITIONAL_FEE);
  }
}
