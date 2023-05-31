package subway.domain.customer;

public class Customer {

    private final AgeGroup ageGroup;

    public Customer(int age) {
        this.ageGroup = AgeGroup.from(age);
    }

    public int getDiscountedPrice(int price) {
        int deductedPrice = price - ageGroup.getDeductPrice();
        double rateToMultiply = 1 - ageGroup.getDiscountRate();
        return (int) (deductedPrice * rateToMultiply);
    }
}
