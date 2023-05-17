package subway.domain.fare;

@FunctionalInterface
public interface DistancePolicy {
    int calculate(int distance);
}
