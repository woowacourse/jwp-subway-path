package subway.domain.charge;

import java.util.List;
import subway.domain.vo.Charge;
import subway.domain.line.Line;

public class DefaultLineChargePolicy implements LineChargePolicy {
    private static final int NO_CHARGE = 0;

    @Override
    public Charge apply(List<Line> linesInRoute) {
        double charge = linesInRoute.stream()
                .mapToDouble(line -> line.getExtraCharge().getValue())
                .distinct()
                .max()
                .orElse(NO_CHARGE);
        return new Charge(charge);
    }
}
