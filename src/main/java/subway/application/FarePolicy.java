package subway.application;

import subway.domain.general.Money;

public interface FarePolicy {
    Money getFareFrom(double distance);
}
