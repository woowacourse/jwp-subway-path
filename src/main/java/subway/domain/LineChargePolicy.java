package subway.domain;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class LineChargePolicy implements SubwayChargePolicy {

    private static final Map<String, Integer> priceMap
            = Map.of("1호선", 500,
                     "2호선", 1000);

    @Override
    public int calculate(final Route route) {

        final List<EdgeSection> shortestRouteSections = route.findShortestRouteSections();

        return shortestRouteSections.stream()
                                    .mapToInt(it -> priceMap.getOrDefault(it.getLineName(), 0))
                                    .max()
                                    .orElse(0);
    }
}
