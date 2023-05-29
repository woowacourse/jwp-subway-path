package subway.domain.fare;

import java.util.List;
import org.springframework.stereotype.Component;
import subway.domain.routestrategy.SubwaySection;

@Component
public class BasicDistanceFareStrategy implements FareStrategy {
    
    private static final int BASE_FARE = 1250;
    
    
    @Override
    public int calculateFare(List<SubwaySection> route) {
        return BASE_FARE + getLineAdditionalFare(route);
    }
    
    private int getLineAdditionalFare(List<SubwaySection> route) {
        return route.stream()
                .mapToInt(SubwaySection::getCharge)
                .max().getAsInt();
    }
}
