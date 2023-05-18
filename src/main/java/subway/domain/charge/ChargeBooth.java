package subway.domain.charge;

import java.util.List;
import org.jgrapht.GraphPath;
import subway.domain.Station;
import subway.domain.WeightedEdgeWithLine;

public class ChargeBooth {

    private final DistanceChargePolicy distanceChargePolicy;
    private final LineChargePolicy lineChargePolicy;
    private final AgeDiscountPolicy ageDiscountPolicy;

    public ChargeBooth() {
        this.distanceChargePolicy = new DefaultDistanceChargePolicy();
        this.lineChargePolicy = new DefaultLineChargePolicy();
        this.ageDiscountPolicy = new DefaultAgeDiscountPolicy();
    }

    public Charge calculateCharge(int passengerAge, GraphPath<Station, WeightedEdgeWithLine> shortestRoute) {
        double totalDistance = shortestRoute.getWeight();
        Charge distanceCharge = distanceChargePolicy.apply(totalDistance);

        List<WeightedEdgeWithLine> edges = shortestRoute.getEdgeList();
        Charge lineCharge = lineChargePolicy.apply(edges);

        return ageDiscountPolicy.apply(passengerAge, distanceCharge.add(lineCharge));
    }
}
