package subway.domain.charge;

public interface AgeDiscountPolicy {
    Charge apply(int passengerAge, Charge charge);
}
