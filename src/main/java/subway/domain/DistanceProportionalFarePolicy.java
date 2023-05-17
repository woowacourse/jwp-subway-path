package subway.domain;

import static java.lang.Math.ceil;

import java.util.List;
import org.springframework.stereotype.Component;
import subway.domain.section.Section;

@Component
public class DistanceProportionalFarePolicy implements FarePolicy {

    private static final int DEFAULT_FARE = 1250;

    @Override
    public Fare calculate(final List<Section> sections) {
        final Integer totalDistance = sections.stream()
                .map(Section::getDistance)
                .map(Distance::getValue)
                .reduce(0, Integer::sum);
        return calculateFare(totalDistance);
    }

    private Fare calculateFare(int distance) {
        final Fare totalFare = new Fare(DEFAULT_FARE);

        if (distance > 50) {
            final double overStandardDistance = distance - 50;
            final int additionalFare = (int) ceil(overStandardDistance / 8) * 100;
            totalFare.sum(additionalFare);
            distance = 50;
        }
        if (distance > 10) {
            final double overStandardDistance = distance - 10;
            final int additionalFare = (int) ceil(overStandardDistance / 5) * 100;
            totalFare.sum(additionalFare);
        }

        return totalFare;
    }
}
