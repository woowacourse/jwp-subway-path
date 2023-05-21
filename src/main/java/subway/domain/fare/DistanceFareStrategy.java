package subway.domain.fare;

public interface DistanceFareStrategy {
    int calculate(final long distance);
}
