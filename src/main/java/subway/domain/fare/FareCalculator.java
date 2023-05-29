package subway.domain.fare;

import java.util.List;
import org.springframework.stereotype.Component;
import subway.domain.Fare;
import subway.domain.routestrategy.SubwaySection;

@Component
public class FareCalculator {
    
    private final List<FareStrategy> fareStrategies;
    private final DiscountStrategy discountStrategy;
    
    
    public FareCalculator(List<FareStrategy> fareStrategies, DiscountStrategy discountStrategy) {
        this.fareStrategies = fareStrategies;
        this.discountStrategy = discountStrategy;
    }
    
    public Fare calculateFare(PassengerAge age, List<SubwaySection> route) {
        int fare = 0;
        for(FareStrategy fareStrategy: fareStrategies) {
            fare += fareStrategy.calculateFare(route);
        }
        fare = discountStrategy.calculateDiscount(fare, age);
        return new Fare(fare);
    }
    
}
