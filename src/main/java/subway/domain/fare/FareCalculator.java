package subway.domain.fare;

import java.util.List;
import org.springframework.stereotype.Component;
import subway.domain.Fare;
import subway.domain.routestrategy.SubwaySection;

@Component
public class FareCalculator {
    
    private final List<FareStrategy> fareStrategies;
    
    public FareCalculator(List<FareStrategy> fareStrategies) {
        this.fareStrategies = fareStrategies;
    }
    
    public Fare calculateFare(List<SubwaySection> route) {
        int fare = 0;
        for(FareStrategy fareStrategy: fareStrategies) {
            fare += fareStrategy.calculateFare(route);
        }
        return new Fare(fare);
    }
    
}
