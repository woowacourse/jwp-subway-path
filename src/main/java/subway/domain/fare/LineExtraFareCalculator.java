package subway.domain.fare;

import java.util.Set;
import subway.domain.line.Line;

public class LineExtraFareCalculator {

    public Fare calculateLineExtraFare(final Set<Line> passingLines) {
        final int extraFare = passingLines.stream()
                .map(Line::getExtraFare)
                .mapToInt(Fare::getFare)
                .max().orElse(0);

        return new Fare(extraFare);
    }
}
