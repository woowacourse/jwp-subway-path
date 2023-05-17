package subway.domain.fare;

import java.util.Comparator;
import java.util.List;
import subway.domain.path.PathFindResult;
import subway.domain.path.SectionEdge;

public class BaseFarePolicy implements FarePolicy {

    private static final int BASE_AMOUNT = 1250;

    @Override
    public int calculate(final PathFindResult result, final Passenger passenger, final int fare) {
        return fare + BASE_AMOUNT + calculateMaxSurcharge(result.getPath());
    }

    private int calculateMaxSurcharge(final List<SectionEdge> path) {
        return path.stream()
                .map(SectionEdge::getSurcharge)
                .max(Comparator.naturalOrder())
                .orElse(0);
    }
}
