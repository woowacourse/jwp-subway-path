package subway.domain.general;

public class Age {
    private final int age;

    private Age(int age) {
        validate(age);
        this.age = age;
    }

    public static Age of(int age) {
        return new Age(age);
    }

    private void validate(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("나이는 0 이상의 정수이어야 합니다.");
        }
    }

    public boolean isSameOrOver(int other) {
        return this.age >= other;
    }

    public boolean isOver(int other) {
        return this.age > other;
    }

    public boolean isSmaller(int other) {
        return this.age < other;
    }

    public int getAge() {
        return age;
    }
}
