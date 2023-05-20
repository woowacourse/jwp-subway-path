package subway.domain.fare;

public interface DiscountPolicy {

    int calculate(final int fare, final FareInformation fareInformation);
}
