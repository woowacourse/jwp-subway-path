package subway.domain.fare;

import java.util.List;
import subway.domain.Distance;
import subway.domain.Fare;

public class FareCalculator {
    
    private final List<FareStrategy> fareStrategies;
    
    public FareCalculator(List<FareStrategy> fareStrategies) {
        this.fareStrategies = fareStrategies;
    }
    
    public Fare calculateFare(Distance distance) {
        int fare = 0;
        for(FareStrategy fareStrategy: fareStrategies) {
            fare += fareStrategy.calculteFare(distance);
        }
        return new Fare(fare);
    }
    
}
