package subway.domain.fare;

public class DistanceFareStrategy implements FareStrategy {

    private static final int BASE_FARE = 1250;

    @Override
    public int calculateFare(final int distance) {
        int fare = BASE_FARE;
        if (distance > 10) { // 10km 초과인 경우
            if (distance <= 50) { // 10km~50km 이내인 경우
                return fare + (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
            }
            return fare + (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
        }
        return fare;
    }
}
