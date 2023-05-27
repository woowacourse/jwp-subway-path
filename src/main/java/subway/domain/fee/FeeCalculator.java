package subway.domain.fee;

import subway.domain.Distance;

public interface FeeCalculator {

    Fee calculate(final Distance distance);
}
