package subway.domain;

public interface FareStrategy {

    boolean isApplicable(Distance distance);

    Fare calculate(Distance distance);
}
