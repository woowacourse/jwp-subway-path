package subway.domain.fare;

import java.util.Arrays;
import subway.exception.SubwayIllegalArgumentException;

public enum AgeFareRate {
    ADULT(19, Integer.MAX_VALUE, 0, 0),
    TEENAGER(13, 18, 350, 20),
    CHILD(6, 12, 350, 50),
    INFANT(0, 5, 0, 100),
    ;

    private final int minAge;
    private final int maxAge;
    private final int discountFare;
    private final int discountRate;

    AgeFareRate(final int minAge, final int maxAge, final int discountFare, final int discountRate) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.discountFare = discountFare;
        this.discountRate = discountRate;
    }

    public static AgeFareRate from(final Integer age) {
        if (age == null) {
            return ADULT;
        }
        return Arrays.stream(values())
                .filter(ageFareRate -> ageFareRate.minAge <= age && age <= ageFareRate.maxAge)
                .findAny()
                .orElseThrow(() -> new SubwayIllegalArgumentException("요금 책정이 불가한 나이입니다."));
    }

    public Fare getDiscountFare() {
        return new Fare(discountFare);
    }

    public int getDiscountRate() {
        return discountRate;
    }
}
