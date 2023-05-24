package subway.domain.discount;

public class Age {
    private static final int MAX_AGE = 150;
    private static final int MIN_KIDS_AGE = 6;
    private static final int MAX_KIDS_AGE = 13;
    private static final int MIN_TEENAGERS_AGE = 13;
    private static final int MAX_TEENAGERS_AGE = 19;
    private final int age;

    public Age(int age) {
        validateAge(age);
        this.age = age;
    }

    private void validateAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("나이는 음수가 될 수 없습니다.");
        }
        if (age > MAX_AGE) {
            throw new IllegalArgumentException("나이는 최대 " + MAX_AGE + "살까지 가능합니다.");
        }
    }

    public boolean isKids() {
        return age >= MIN_KIDS_AGE && age < MAX_KIDS_AGE;
    }

    public boolean isTeenagers() {
        return age >= MIN_TEENAGERS_AGE && age < MAX_TEENAGERS_AGE;
    }

    public int getAge() {
        return age;
    }
}
