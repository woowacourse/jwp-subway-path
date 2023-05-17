package subway.domain.fare;

public class Passenger {

    private AgeGroup ageGroup;

    public Passenger(final int age) {
        this.ageGroup = AgeGroup.from(age);
    }

    public int calculateFare(final int fare) {
        return ageGroup.calculateFare(fare);
    }
}
