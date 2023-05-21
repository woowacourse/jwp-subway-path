package subway.domain.Path;

import subway.domain.policy.discount.DiscountPolicy;

import java.util.Objects;

public class Fare {

    private final int fare;

    private Fare(int fare) {
        this.fare = fare;
    }

    public static Fare from(int fare) {
        return new Fare(fare);
    }

    public Fare applyDiscount(DiscountPolicy discountPolicy) {
        return Fare.from(discountPolicy.calculate(fare));
    }

    public int getFare() {
        return fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fare other = (Fare) o;
        return fare == other.fare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }
}
