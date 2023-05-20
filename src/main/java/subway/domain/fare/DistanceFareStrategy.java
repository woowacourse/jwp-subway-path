package subway.domain.fare;

public class DistanceFareStrategy implements FareStrategy {

    private static final int BASE_FARE = 1250;

    @Override
    public int calculateFare(final int distance) {
        int surcharge = 0;
        if (distance > 10) { // 10km 초과인 경우
            if (distance <= 50) { // 10km~50km 이내인 경우
                surcharge += (int) ((Math.floor((distance - 10 - 1) / 5.0) + 1) * 100);
            }

            if (distance > 50) {
                surcharge = Math.min((int) ((Math.floor((distance - 10 - 1) / 5.0) + 1) * 100), 800);
                surcharge += (int) ((Math.floor((distance - 50 - 1) / 8.0) + 1) * 100);
            }
        }
        return BASE_FARE + surcharge;
    }
}
