package subway.domain.fare;

public class DiscountFare {

    private static final int TEENAGER_DISCOUNT_RATE = 20, CHILD_DISCOUNT_RATE = 50;
    private static final Fare TAX = new Fare(350);

    private final Fare basicFare;

    public DiscountFare(final Fare basicFare) {
        this.basicFare = basicFare;
    }

    public Fare calculateTeenagerFare() {
        final Fare deductedFare = basicFare.subtract(TAX);
        return deductedFare.multiply(new Fare((100 - TEENAGER_DISCOUNT_RATE) * 0.01));
    }

    public Fare calculateChildFare() {
        final Fare deductedFare = basicFare.subtract(TAX);
        return deductedFare.multiply(new Fare((100 - CHILD_DISCOUNT_RATE) * 0.01));
    }
}

