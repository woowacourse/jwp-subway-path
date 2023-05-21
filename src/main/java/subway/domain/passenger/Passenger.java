package subway.domain.passenger;

public final class Passenger {

    private final Age age;

    public Passenger(final int age) {
        this.age = new Age(age);
    }

    public Age getAge() {
        return age;
    }
}
