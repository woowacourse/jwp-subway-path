package subway.domain.fare.strategy.charge;

import subway.domain.fare.FareInfo;

public interface ChargeStrategy {
    int calculate(FareInfo fareInfo);
}
