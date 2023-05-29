package subway.application.fare;

import java.util.Set;
import org.springframework.stereotype.Component;
import subway.domain.fare.Fare;
import subway.domain.line.Line;

@Component
public class LineExtraFareCalculator {

    public Fare calculateLineExtraFare(final Set<Line> passingLines) {
        final int extraFare = passingLines.stream()
                .map(Line::getExtraFare)
                .mapToInt(Fare::getFare)
                .max().orElse(0);

        return new Fare(extraFare);
    }
}
