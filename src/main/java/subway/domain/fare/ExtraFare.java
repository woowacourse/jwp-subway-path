package subway.domain.fare;

import java.util.List;
import subway.domain.line.LineWithSectionRes;

public class ExtraFare {

    private final Fare fare;

    public ExtraFare(final List<LineWithSectionRes> possibleSections) {
        this.fare = getMaxLineExtraFare(possibleSections);
    }

    private Fare getMaxLineExtraFare(final List<LineWithSectionRes> possibleSections) {
        final int maxFare = possibleSections.stream()
            .mapToInt(LineWithSectionRes::getExtraFare)
            .max().orElse(0);
        return new Fare(maxFare);
    }

    public Fare fare() {
        return fare;
    }
}
