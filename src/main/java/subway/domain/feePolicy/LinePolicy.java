package subway.domain.feePolicy;

import subway.domain.Line;
import subway.exception.LineNotFoundException;
import java.util.List;
import java.util.OptionalInt;

public class LinePolicy {

    private static final String EMPTY_LINE_MESSAGE = "통과한 노선이 존재하지 않습니다.";

    public int calculateExtraFee(final List<Line> passLines) {
        OptionalInt maxExtraFeeByLine = passLines.stream()
                .mapToInt(Line::getExtraFee)
                .max();

        if (maxExtraFeeByLine.isPresent()) {
            return maxExtraFeeByLine.getAsInt();
        }
        throw new LineNotFoundException(EMPTY_LINE_MESSAGE);
    }
}
