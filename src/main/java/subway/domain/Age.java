package subway.domain;

import java.util.Arrays;
import java.util.Objects;

public enum Age {

    ADULT("adult", 0.0),
    TEENAGER("teenager", 0.2),
    CHILDREN("children", 0.5);

    private final String age;
    private final double discountRate;

    Age(final String age, final double discountRate) {
        this.age = age;
        this.discountRate = discountRate;
    }

    public static Age searchAge(final String age) {
        return Arrays.stream(values()).filter(each -> each.age.equals(age))
                .findFirst()
                .orElseThrow(()->new IllegalArgumentException("[ERROR] 연령대가 잘못 입력되었습니다."));
    }

    public String getAge() {
        return age;
    }

    public double getDiscountRate() {
        return discountRate;
    }
}
