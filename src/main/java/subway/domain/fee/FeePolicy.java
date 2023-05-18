package subway.domain.fee;

public interface FeePolicy {

    int calculate(final int distance);
}
