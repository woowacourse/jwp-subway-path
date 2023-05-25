package subway.line.domain.fare.application.domain;

public class Age {
    private final int age;

    private Age(int age) {
        this.age = age;
    }

    public static Age of(int age) {
        return new Age(age);
    }

    public boolean isLessThanOrEquals(Age age) {
        return this.age <= age.age;
    }

    public boolean isMoreThanOrEquals(Age age) {
        return this.age >= age.age;
    }
}
