package subway.domain.fare;

import java.util.Set;
import subway.common.exception.SubwayIllegalArgumentException;
import subway.domain.line.Line;
import subway.domain.route.Route;

public class LineSurchargePolicy implements FarePolicy {

    @Override
    public Fare calculate(final Route route, final Integer age, final Fare fare) {
        Set<Line> lines = route.findLines();
        Fare maxLineSurcharge = findMaxLineSurcharge(lines);
        return fare.plus(maxLineSurcharge);
    }

    private Fare findMaxLineSurcharge(final Set<Line> lines) {
        return lines.stream()
                .map(Line::getSurcharge)
                .max(Fare::compareTo)
                .orElseThrow(() -> new SubwayIllegalArgumentException("경로에 구간이 없습니다."));
    }
}
