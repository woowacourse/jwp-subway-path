package subway.path.domain;

public enum AgeGroup {

    TODDLER("영유아", 0, 6), // 유아
    KIDS("어린이", 6, 13),
    TEENAGERS("청소년", 13, 18),
    ADULTS("성인", 19, 65),
    SENIOR("노인", 65, Integer.MAX_VALUE),
    ;

    private final String info;
    private final int minInclusiveAge;
    private final int maxExclusiveAge;

    AgeGroup(final String info, final int minInclusiveAge, final int maxExclusiveAge) {
        this.info = info;
        this.minInclusiveAge = minInclusiveAge;
        this.maxExclusiveAge = maxExclusiveAge;
    }

    public String info() {
        return info;
    }

    public int minInclusiveAge() {
        return minInclusiveAge;
    }

    public int maxExclusiveAge() {
        return maxExclusiveAge;
    }
}
