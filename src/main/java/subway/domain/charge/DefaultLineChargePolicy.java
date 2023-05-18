package subway.domain.charge;

import java.util.List;
import subway.domain.WeightedEdgeWithLine;

public class DefaultLineChargePolicy implements LineChargePolicy {
    private static final int NO_CHARGE = 0;

    @Override
    public Charge apply(List<WeightedEdgeWithLine> edges) {
        Double charge = edges.stream()
                .mapToDouble(it -> it.getLine().getExtraCharge().getValue())
                .distinct()
                .max()
                .orElse(NO_CHARGE);
        return new Charge(charge);
    }
}
