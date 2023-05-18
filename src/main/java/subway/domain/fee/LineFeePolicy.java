package subway.domain.fee;

import java.util.Comparator;
import subway.domain.Line;

public class LineFeePolicy implements FeePolicy {

    @Override
    public int calculate(final FeeInformation feeInformation) {
        return feeInformation.getLines().getLines().stream()
                .map(Line::getAdditionalFee)
                .max(Comparator.naturalOrder())
                .orElse(0);
    }
}
