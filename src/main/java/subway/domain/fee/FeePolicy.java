package subway.domain.fee;

public interface FeePolicy {

    int calculate(final FeeInformation feeInformation);
}
