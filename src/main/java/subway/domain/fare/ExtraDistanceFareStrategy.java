package subway.domain.fare;

import java.util.List;
import org.springframework.stereotype.Component;
import subway.domain.Distance;
import subway.domain.routestrategy.SubwaySection;

@Component
public class ExtraDistanceFareStrategy implements FareStrategy {
    
    private static final Distance BASE_DISTANCE = new Distance(10);
    private static final Distance EXTRA_DISTANCE = new Distance(50);
    private static final int UNIT_DISTANCE_UNDER_EXTRA_DISTANCE = 5;
    private static final int UNIT_DISTANCE_OVER_EXTRA_DISTANCE = 8;
    private static final int EXTRA_FARE = 100;
    
    
    @Override
    public int calculateFare(List<SubwaySection> route) {
        int fare = 0;
        Distance distance = getDistanceOfPath(route); //todo 추가한거
        if (distance.isLongerThan(EXTRA_DISTANCE)) {
            Distance overExtraDistance = distance.subtract(EXTRA_DISTANCE);
            distance = distance.subtract(overExtraDistance);
            fare += (int) (Math.ceil((overExtraDistance.getValue()-1) / UNIT_DISTANCE_OVER_EXTRA_DISTANCE)+1) * EXTRA_FARE;
        }
        
        if(distance.isLongerThan(BASE_DISTANCE)) {
            int underExtraDistance = distance.subtract(BASE_DISTANCE).getValue();
            fare += (int) (Math.ceil((underExtraDistance-1) / UNIT_DISTANCE_UNDER_EXTRA_DISTANCE)+1) * EXTRA_FARE;
        }
        
        return fare;
    }
    
    private Distance getDistanceOfPath(List<SubwaySection> route) {
        return route.stream()
                .map(SubwaySection::getDistance)
                .reduce(Distance::plus)
                .get();
    }
}
