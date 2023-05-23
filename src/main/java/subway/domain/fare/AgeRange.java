package subway.domain.fare;

public class AgeRange {

    private final static int MIN_CHARGE = 350;

    private final int min;
    private final int max;
    private final int discountRate;

    public AgeRange(int min, int max, int discountRate) {
        this.min = min;
        this.max = max;
        this.discountRate = discountRate;
    }

    public boolean isInRange(int age) {
        return age >= min && age <= max;
    }

    public int getDiscountAmount(int fare) {
        return (fare - MIN_CHARGE) * discountRate / 100;
    }
}
