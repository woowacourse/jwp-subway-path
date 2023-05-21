package subway.domain.fare;

import java.util.Comparator;
import subway.domain.path.Path;
import subway.domain.path.SectionEdge;

public class BaseFarePolicy implements FarePolicy {

    private static final int BASE_AMOUNT = 1250;

    @Override
    public int calculate(final Path path, final Passenger passenger, final int fare) {
        return fare + BASE_AMOUNT + calculateMaxSurcharge(path);
    }

    private int calculateMaxSurcharge(final Path path) {
        return path.getSectionEdges().stream()
                .map(SectionEdge::getSurcharge)
                .max(Comparator.naturalOrder())
                .orElse(0);
    }
}
