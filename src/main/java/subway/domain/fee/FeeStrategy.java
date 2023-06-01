package subway.domain.fee;

import subway.domain.subway.Distance;

public interface FeeStrategy {
    int calculateFee(Distance distance);
}
