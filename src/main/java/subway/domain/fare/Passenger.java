package subway.domain.fare;

public class Passenger {

    private int age;

    public Passenger(final int age) {
        this.age = age;
    }

    public AgeGroup calulateAgeGroup() {
        return AgeGroup.from(age);
    }
}
