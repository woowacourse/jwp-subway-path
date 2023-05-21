package subway.domain;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class LineFarePolicy implements SubwayFarePolicy {

    private static final Map<String, Integer> priceMap
            = Map.of("1호선", 500,
                     "2호선", 1000);

    @Override
    public Money calculate(final Route route) {

        final List<EdgeSection> shortestRouteSections = route.findShortestRouteSections();

        return shortestRouteSections.stream()
                                    .reduce(Money.ZERO, (money, edgeSection) ->
                                                    new Money(priceMap.getOrDefault(edgeSection.getLineName(), 0)),
                                            (Money::max));
    }
}
