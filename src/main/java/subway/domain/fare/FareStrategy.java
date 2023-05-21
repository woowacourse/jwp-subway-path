package subway.domain.fare;

public interface FareStrategy {
    Fare calculate(int value);
}
