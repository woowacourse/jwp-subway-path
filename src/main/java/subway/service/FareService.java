package subway.service;

import org.springframework.stereotype.Service;
import subway.domain.Distance;
import subway.domain.Fare;
import subway.domain.FareCalculator;
import subway.domain.Path;

@Service
public class FareService {
    private final FareCalculator fareCalculator;

    public FareService(FareCalculator fareCalculator) {
        this.fareCalculator = fareCalculator;
    }

    public Fare calculateFareOf(Path path) {
        Distance distance = path.calculateTotalDistance();
        return fareCalculator.calculate(distance);
    }
}
