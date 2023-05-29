package subway.application.fare;

import java.util.Set;
import org.springframework.stereotype.Component;
import subway.domain.fare.Fare;
import subway.domain.line.Line;
import subway.domain.path.SubwayPath;

@Component
public class ExtraFarePolicy {

    private final DistanceFareCalculator distanceFareCalculator;

    private final LineExtraFareCalculator lineExtraFareCalculator;

    public ExtraFarePolicy(
            final DistanceFareCalculator distanceFareCalculator,
            final LineExtraFareCalculator lineExtraFareCalculator
    ) {
        this.distanceFareCalculator = distanceFareCalculator;
        this.lineExtraFareCalculator = lineExtraFareCalculator;
    }

    public Fare calculateExtraFare(final SubwayPath subwayPath) {
        final int distance = subwayPath.getDistance();
        final Fare distanceExtraFare = distanceFareCalculator.calculateFareByDistance(distance);

        final Set<Line> passingLines = subwayPath.getPassingLine();
        final Fare lineExtraFare = lineExtraFareCalculator.calculateLineExtraFare(passingLines);

        return distanceExtraFare.add(lineExtraFare);
    }
}
