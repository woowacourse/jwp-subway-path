package subway.domain.fare;

import subway.domain.Distance;

public class BasicDistanceFareStrategy implements FareStrategy {
    
    private static final int BASE_FARE = 1250;
    
    @Override
    public int calculteFare(Distance distance) {
        return BASE_FARE;
    }
    
}
