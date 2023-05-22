package subway.domain.charge;

import java.util.List;
import subway.domain.vo.Charge;
import subway.domain.vo.Distance;
import subway.domain.line.Line;

public class ChargeBooth {

    private final DistanceChargePolicy distanceChargePolicy;
    private final LineChargePolicy lineChargePolicy;
    private final AgeDiscountPolicy ageDiscountPolicy;

    public ChargeBooth() {
        this.distanceChargePolicy = new DefaultDistanceChargePolicy();
        this.lineChargePolicy = new DefaultLineChargePolicy();
        this.ageDiscountPolicy = new DefaultAgeDiscountPolicy();
    }

    public Charge calculateCharge(int passengerAge, Distance totalDistance, List<Line> linesInRoute) {
        Charge distanceCharge = distanceChargePolicy.apply(totalDistance);
        Charge lineCharge = lineChargePolicy.apply(linesInRoute);
        return ageDiscountPolicy.apply(passengerAge, distanceCharge.add(lineCharge));
    }
}
