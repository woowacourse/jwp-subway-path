package subway.domain.fare;

import subway.domain.fare.Fare;

public class DiscountFare {

    private final Fare childFare;
    private final Fare teenagerFare;

    public DiscountFare(final Fare childFare, final Fare teenagerFare) {
        this.childFare = childFare;
        this.teenagerFare = teenagerFare;
    }

    public Fare getTeenagerFare() {
        return teenagerFare;
    }

    public Fare getChildFare() {
        return childFare;
    }
}
