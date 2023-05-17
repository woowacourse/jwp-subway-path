package subway.domain;

import java.util.Arrays;

public enum Age {

    ADULT("adult", 0, 0.0),
    TEENAGER("teenager", 350, 0.2),
    CHILDREN("children", 350, 0.5);

    private final String age;
    private final int deduction;
    private final double discountRate;

    Age(final String age, final int deduction, final double discountRate) {
        this.age = age;
        this.deduction = deduction;
        this.discountRate = discountRate;
    }

    public static Age searchAge(final String age) {
        return Arrays.stream(values()).filter(each -> each.age.equals(age))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 연령대가 잘못 입력되었습니다."));
    }

    public String getAge() {
        return age;
    }

    public int getDeduction() {
        return deduction;
    }

    public double getDiscountRate() {
        return discountRate;
    }
}
