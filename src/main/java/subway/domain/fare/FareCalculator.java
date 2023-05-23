package subway.domain.fare;

import subway.domain.fare.strategy.FareStrategy;

import java.util.List;

public final class FareCalculator {
    private final List<FareStrategy> fareStrategies;

    public FareCalculator(final List<FareStrategy> fareStrategies) {
        this.fareStrategies = fareStrategies;
    }

    public FareInfo of(FareInfo fareInfo) {
        for (final FareStrategy fareStrategy : fareStrategies) {
            fareInfo = fareStrategy.calculate(fareInfo);
        }

        return fareInfo;
    }
}
