package subway.domain.fare;

import org.springframework.stereotype.Component;

@Component
public class BaseFarePolicy {

    private static final int BASE_FARE = 1250;

    public Fare getBaseFare() {
        return new Fare(BASE_FARE);
    }
}
