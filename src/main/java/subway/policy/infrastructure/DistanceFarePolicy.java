package subway.policy.infrastructure;

import static subway.value_object.Distance.DEFAULT_DISTANCE;
import static subway.value_object.Distance.MID_DISTANCE;

import java.math.BigDecimal;
import java.util.List;
import subway.policy.domain.SubwayFarePolicy;
import subway.value_object.Distance;
import subway.value_object.Money;
import subway.line.domain.Line;
import subway.route.application.JgraphtRouteFinder;
import subway.line.domain.Station;

public class DistanceFarePolicy implements SubwayFarePolicy {

  private static final Money DEFAULT_PRICE = new Money(1250);
  private static final BigDecimal ADDITIONAL_FEE = BigDecimal.valueOf(100);
  private static final int MID_DISTANCE_RATE = 5;
  private static final int LONG_DISTANCE_RATE = 8;

  @Override
  public Money calculate(final List<Line> lines, final Station departure,
      final Station arrival) {
    final Distance distance =
        JgraphtRouteFinder.findShortestRouteDistance(lines, departure, arrival);

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
