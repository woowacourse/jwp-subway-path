package subway.domain.fare.service;

import org.springframework.stereotype.Component;
import subway.domain.fare.AgeFarePolicy;
import subway.domain.fare.DistanceFarePolicy;
import subway.domain.fare.LineFarePolicy;
import subway.domain.line.Line;
import subway.domain.line.LineRepository;
import subway.domain.path.Path;

@Component
public class FareCalculateService {

    private static final int STANDARD_FARE = 1_250;

    private final LineRepository lineRepository;

    public FareCalculateService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public int calculate(final Path path, final int age) {
        final Long distance = path.getDistance();
        final int maxAdditionalFare = getMaxAdditionalFare(path);

        final DistanceFarePolicy distanceFarePolicy = new DistanceFarePolicy();
        final LineFarePolicy lineFarePolicy = new LineFarePolicy();
        final AgeFarePolicy ageFarePolicy = AgeFarePolicy.from(age);

        int calculatedFare = STANDARD_FARE;

        calculatedFare = distanceFarePolicy.calculate(distance, calculatedFare);
        calculatedFare = lineFarePolicy.calculate(maxAdditionalFare, calculatedFare);
        calculatedFare = ageFarePolicy.calculate(calculatedFare);

        return calculatedFare;
    }

    private int getMaxAdditionalFare(final Path path) {
        return path.getSectionEdges().stream()
                .mapToInt(sectionEdge -> {
                    final Long lineId = sectionEdge.getLineId();
                    final Line line = lineRepository.findById(lineId);
                    return line.getAdditionalFare();
                })
                .max()
                .orElse(0);
    }
}