package subway.domain;

import java.util.Arrays;

public enum AgeGroup {

    OLD(65, Integer.MAX_VALUE, 0, 1),
    ADULT(19, 64, 0, 0.0),
    TEENAGER(13,18, 350, 0.2),
    CHILDREN(6, 12, 350, 0.5),
    INFANT(0, 5, 0, 1);

    private final int minAge;
    private final int maxAge;
    private final int deduction;
    private final double discountRate;

    AgeGroup(final int minAge, final int maxAge, final int deduction, final double discountRate) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.deduction = deduction;
        this.discountRate = discountRate;
    }

    public static AgeGroup matchAgeGroup(final int age) {
        if(age < 0){
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 숫자입니다.");
        }
        return Arrays.stream(values()).filter(each -> each.minAge<= age && age <= each.maxAge)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("[ERROR] 연령대 매칭에 실패하였습니다."));
    }

    public int getMinAge() {
        return minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public int getDeduction() {
        return deduction;
    }

    public double getDiscountRate() {
        return discountRate;
    }
}
