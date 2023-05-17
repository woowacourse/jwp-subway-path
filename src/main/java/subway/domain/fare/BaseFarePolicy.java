package subway.domain.fare;

import subway.domain.path.PathFindResult;

public class BaseFarePolicy implements FarePolicy {

    private static final int BASE_AMOUNT = 1250;

    @Override
    public int calculate(final PathFindResult result, final Passenger passenger, final int fare) {
        return fare + BASE_AMOUNT;
    }
}
