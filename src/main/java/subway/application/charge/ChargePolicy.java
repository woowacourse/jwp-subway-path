package subway.application.charge;


public interface ChargePolicy {
    int calculateFee(int distance);
}
