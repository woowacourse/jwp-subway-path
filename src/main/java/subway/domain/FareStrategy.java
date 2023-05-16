package subway.domain;

public interface FareStrategy {
    int calculate(final long distance);
}
