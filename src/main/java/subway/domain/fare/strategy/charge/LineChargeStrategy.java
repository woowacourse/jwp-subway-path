package subway.domain.fare.strategy.charge;

import java.util.Comparator;
import java.util.Set;

import subway.domain.exception.DomainException;
import subway.domain.exception.ExceptionType;
import subway.domain.fare.FareInfo;
import subway.domain.subway.Line;

public class LineChargeStrategy implements ChargeStrategy {
    @Override
    public int calculate(FareInfo fareInfo) {
        Set<Line> lines = fareInfo.getLines();
        Line maximumAdditionalFareLine = getMaximumAdditionalFareLine(lines);
        return maximumAdditionalFareLine.getAdditionalFare();
    }

    private Line getMaximumAdditionalFareLine(Set<Line> lines) {
        return lines.stream()
            .max(Comparator.comparingInt(Line::getAdditionalFare))
            .orElseThrow(() -> new DomainException(ExceptionType.NO_LINE));
    }
}
