package subway.domain.fare;

import java.util.Comparator;
import subway.domain.Line;

public class LineFarePolicy implements FarePolicy {

    @Override
    public int calculate(final FareInformation fareInformation) {
        return fareInformation.getLines().getLines().stream()
                .map(Line::getAdditionalFee)
                .max(Comparator.naturalOrder())
                .orElse(0);
    }
}
