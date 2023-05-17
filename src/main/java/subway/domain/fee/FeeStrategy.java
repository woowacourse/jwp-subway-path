package subway.domain.fee;

public interface FeeStrategy {
    int calculate(int distance);
}
