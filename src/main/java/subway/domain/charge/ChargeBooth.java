package subway.domain.charge;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import subway.domain.Line;
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

        List<Line> linesInRoute = shortestRoute.getEdgeList().stream()
                .map(edge -> edge.getLine())
                .collect(Collectors.toList());
        Charge lineCharge = lineChargePolicy.apply(linesInRoute);

        return ageDiscountPolicy.apply(passengerAge, distanceCharge.add(lineCharge));
    }
}
