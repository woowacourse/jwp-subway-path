package subway.domain.fare;

public class LineFarePolicy {

    public int calculate(final int maxAdditionalFare, final int fare) {
        return fare + maxAdditionalFare;
    }
}
